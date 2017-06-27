/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Model;

import static absoluteschedule.Helper.SQLManage.getConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author NicR
 */
public class Calendar {
//Instance Variables
    //Bueiness hours (6am-8pm)
    //Work days (Mon - Fri)
    //Appt Details
    private int apptID;
    private int custID;
    private StringProperty apptTitle;
    private StringProperty apptDesc;
    private StringProperty apptLoc;
    private StringProperty apptContact;  //This is the consultant they are meeting with
    private String apptURL;              //Need to find out what this is for
    private StringProperty apptStartTime;
    private StringProperty apptEndTime;
    
    
//SQL Variables
    private ResultSet rs = null;
    private ResourceBundle localization;
    
//Constructor
    public Calendar(){
        apptID = -1;
        custID = -1;
        apptTitle = new SimpleStringProperty("Change this title");
        apptDesc = new SimpleStringProperty("Change this description");
        apptLoc = new SimpleStringProperty("Change this location");
        apptContact = new SimpleStringProperty("Change this consultant");
        apptURL = "Change this URL";
        apptStartTime = new SimpleStringProperty("change this time");
        apptEndTime = new SimpleStringProperty("change this time");
    }
        
//Setters and Getters
    //ApptID
    public void setApptID(int id){
        apptID = id;
    }
    public int getApptID(){
        return apptID;
    }
    //CustomerID
    public void setCustID(int id){
        custID = id;
    }
    public int getCustID(){
        return custID;
    }
    //Appt Title
    public void setApptTitle(String title){
        apptTitle.set(title);
    }
    public String getApptTitle(){
        return this.apptTitle.get();
    }
    //Appt Description
    public void setApptDesc(String desc){
        apptDesc.set(desc);
    }
    public String getApptDesc(){
        return apptDesc.get();
    }
    //Appt Location
    public void setApptLoc(String loc){
        apptLoc.set(loc);
    }
    public String getApptLoc(){
        return apptLoc.get();
    }
    //Appt Contact
    public void setApptContact(String contact){
        apptContact.set(contact);
    }
    public String getApptContact(){
        return apptContact.get();
    }
    //Appt URL
    public void setApptURL(String url){
        apptURL = url;
    }
    public String getApptURL(){
        return apptURL;
    }
    //Appt Start Time
    public void setApptStartTime(String zonedTime){
        apptStartTime.set(zonedTime);
    }
    public String getApptStartTime(){
        return apptStartTime.get();
    }
    //Appt End Time
    public void setApptEndTime(String zonedTime){
        apptEndTime.set(zonedTime);
    }
    public String getApptEndTime(){
        return apptEndTime.get();
    }

//Check if appointment is already in DB
    public List isApptValid(int custID, String startTime, String endTime, String consultName) throws SQLException{
        System.out.println("Checking if appointment is conflicting with another.");
        
    //Checks Date/Time/Customer/Consultant.
    //If eitheher customer or consultant are matched at the same date/time then those appts are returned
        List<Calendar> tempApptsList = new ArrayList<>();
        
    //Try clause
        try(Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointment WHERE (customerID=? AND (start>=? OR end<=?)) OR (contact=? AND (start>=? OR end<=?));")){
            ps.setInt(1, custID);
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(startTime));
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(endTime));
            ps.setString(4, consultName);
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(startTime));
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(endTime));
            rs = ps.executeQuery();

        //Set variables with data from DB
            while(rs.next()){
                System.out.println("An appointment exists for either customer or consultant");
                Calendar tempAppt = new Calendar();
             
            //Create appointment and add to ArrayList
                tempAppt.setApptID(rs.getInt("appointmentId"));
                tempAppt.setCustID(rs.getInt("customerId"));
                tempAppt.setApptContact("contact");
                tempAppt.setApptStartTime("start");
                tempAppt.setApptEndTime("end");
                tempApptsList.add(tempAppt);
            }
            
            int count = tempApptsList.size();
            System.out.println("Count of Appts: " + count);

        }
        catch(SQLException err){
            err.printStackTrace();
        }

        return tempApptsList;
    }

//Add Appt method
    public void addAppt(int custID, String title, String desc, String loc, String contact, String url, String start, String end, String user) throws SQLException{
        try(Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("INSERT INTO appointment (customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)")){

        //SQL Statement to insert data
            
            ps.setInt(1, custID);
            ps.setString(2, title);
            ps.setString(3, desc);
            ps.setString(4, loc);
            ps.setString(5, contact);
            ps.setString(6, url);
            ps.setTimestamp(7, java.sql.Timestamp.valueOf(start));
            ps.setTimestamp(8, java.sql.Timestamp.valueOf(end));
            ps.setString(9, user);
            ps.setString(10, user);
            ps.execute();
            System.out.println("Appointment: " + title + " was submitted by User: " + user);
        }
        catch(SQLException err){
            
        }
    }
    
//Convert Date/Time for SQL Statement
    public static String convertToUTC(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime passedTime = LocalDateTime.parse(str, formatter);
        ZonedDateTime localTime = ZonedDateTime.of(passedTime, ZoneId.systemDefault());
        ZonedDateTime UTCTime = localTime.withZoneSameInstant(ZoneOffset.UTC);
        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(UTCTime);
        
        return dateTime;
    }
    public static String convertToLocal(String str){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime passedTime = LocalDateTime.parse(str, formatter);
        ZonedDateTime UTCTime = ZonedDateTime.of(passedTime, ZoneId.of("UTC"));
        ZonedDateTime localTime = UTCTime.withZoneSameInstant(ZoneOffset.systemDefault());
        String dateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localTime);

        return dateTime;
    }
}

