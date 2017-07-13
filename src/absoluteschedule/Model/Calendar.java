/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Model;

import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
import static absoluteschedule.Helper.SQLManage.getConn;
import static absoluteschedule.Helper.SQLManage.prepare;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private static ResourceBundle localization = loadResourceBundle();
    
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

//Appt Handlers
    //Check if appointment is already in DB
    public List isApptValid(int custID, String startTime, String endTime, String consultName) throws SQLException{
        System.out.println("Checking if appointment is conflicting with another.");
        
    //Checks Date/Time/Customer/Consultant.
    //If eitheher customer or consultant are matched at the same date/time then those appts are returned
        List<Calendar> tempApptsList = new ArrayList<>();
        
        String sql = "SELECT * FROM appointment WHERE (customerID=? AND (start>=? AND end<=?)) OR (contact=? AND (start>=? AND end<=?));";

    //Try clause
        try(Connection conn = getConn();
            PreparedStatement statement = prepare(conn, sql, ps ->{ps.setInt(1, custID);
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(startTime));
            ps.setTimestamp(3, java.sql.Timestamp.valueOf(endTime));
            ps.setString(4, consultName);
            ps.setTimestamp(5, java.sql.Timestamp.valueOf(startTime));
            ps.setTimestamp(6, java.sql.Timestamp.valueOf(endTime));})){
            rs = statement.executeQuery();

        //Set variables with data from DB
            while(rs.next()){
                //System.out.println("An appointment exists for either customer or consultant");
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
            //System.out.println("Count of Appts: " + count);

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
            err.printStackTrace();
        }
    }
    //Update Appt method
    public void updateAppt(int apptID, int custID, String title, String desc, String loc, String contact, String url, String start, String end, String user) throws SQLException{
        try(Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("UPDATE appointment SET customerId=?, title=?, description=?, location=?, contact=?, "
                                                       + "url=?, start=?, end=?, lastUpdate=NOW(), lastUpdateBy=? WHERE appointmentId=?")){

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
            ps.setInt(10, apptID);
            ps.execute();
            System.out.println("Appointment: " + title + " was updated by User: " + user);
        }
        catch(SQLException err){
            err.printStackTrace();
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
        String dateTime = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss").format(localTime);

        return dateTime;
    }
    
//Check if entry is valid - date, startHour, startMin, endHour, endMin, allDay, customerName, consultantName, location, title, desc
    public static String isEntryValid(String message, String testDate, String testStartHour, String testStartMin, String testEndHour, String testEndMin, boolean testAllDay, String testCustName, String testConsultName, String testLoc, String testTitle, String testDesc){
    //Test Date
        if(testDate.equals("")){
            message = message + localization.getString("cal_valid_date");
        }
    
    //Check if date is M-F
        String dateSub = testDate.substring(0,10);
        LocalDate date = LocalDate.parse(dateSub);
        if(date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)){
            message = message + localization.getString("cal_valid_bus_days");
        }
        else{
            System.out.println("Day of week: " + date.getDayOfWeek());
        }
        
    //Times
    //Check if All Day
        if(testAllDay){
        }
        else{
        //Test Start & End Times
            //Start Time
            String tempStart = "0";
            String tempEnd = "0";
            
            try{
                if(testStartHour.equals("") || testStartMin.equals("")){
                    message = message + localization.getString("cal_valid_start");
                }
                else{
                    tempStart = testStartHour+testStartMin;
                    if(Integer.parseInt(tempStart)<600 || Integer.parseInt(tempStart)>2000){
                        message = message + localization.getString("cal_valid_bus_hours_start");
                    }
                }
            }
            catch(NullPointerException e){
                message = message + localization.getString("cal_valid_start");
            }
            
            //End Time
            try{
                if(testEndHour.equals("") || testEndMin.equals("")){
                    message = message + localization.getString("cal_valid_end");
                }
                else{
                    tempEnd = testEndHour+testEndMin;
                    if(Integer.parseInt(tempEnd)<600 || Integer.parseInt(tempEnd)>2000){
                    message = message + localization.getString("cal_valid_bus_hours_end");
                    }
                //Business Hours and Start Time after End time validation
                    if(Integer.parseInt(tempStart)>=Integer.parseInt(tempEnd)){
                    message = message + localization.getString("cal_valid_start_end");
                    }
                }
            }
            catch(NullPointerException e){
                message = message + localization.getString("cal_valid_end");
            }
        }

    //Test Customer Name
        try{
            if(testCustName.equals("")){
                message = message + localization.getString("cal_valid_cust");
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("cal_valid_cust");
        }

    //Test Consultant Name
        try{
            if(testConsultName.equals("")){
                message = message + localization.getString("cal_valid_consult");
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("cal_valid_consult");
        }
        
    //Test Location
        try{
            if(testLoc.equals("")){
                message = message + localization.getString("cal_valid_loc");
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("cal_valid_loc");
        }
        
    //Test Title
        if(testTitle.equals("")){
            message = message + localization.getString("cal_valid_title");
        }
        
    //Test Description
        if(testDesc.equals("")){
            message = message + localization.getString("cal_valid_desc");
        }
        
    //Return Error message
        return message;
    }
}

