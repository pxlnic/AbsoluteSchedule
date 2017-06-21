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
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private String apptURL;              //Need to find otu what this is for
    private StringProperty apptDate;
    private StringProperty apptStartTime;
    private StringProperty apptEndTime;
    
    
//SQL Variables
    private Connection conn;
    private PreparedStatement ps = null;
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
        apptDate = new SimpleStringProperty("Change this date");
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
    public String getApptCotnact(){
        return apptContact.get();
    }
    //Appt URL
    public void setApptURL(String url){
        apptURL = url;
    }
    public String getApptURL(){
        return apptURL;
    }
    //Appt Date
    public void setApptDate(String date){
        apptDate.set(date);
    }
    public String getApptDate(){
        return apptDate.get();
    }
    //Appt Start Time
    public void setApptStartTime(int hour, int min){
        apptStartTime.set(hour + ":" + min);
    }
    public String getApptStartTime(){
        return apptStartTime.get();
    }
    //Appt End Time
    public void setApptEndTime(int hour, int min){
        apptEndTime.set(hour + ":" + min);
    }
    public String getApptEndTime(){
        return apptEndTime.get();
    }

//Check if appointment is already in DB
    public int isApptValid(){
        return apptID;
    }

//Add Appt method
    public void addAppt(String custName, String title, String desc, String loc, String contact, String url, String start, String end, String user) throws SQLException{
        try{
        //Set SQL based variables
            conn = getConn();
            
            Customer tempCust = new Customer();
            int custID = tempCust.isIDValid("Customer", "customerName", "customerId", custName);

        //SQL Statement to insert data
            ps = conn.prepareStatement("INSERT INTO appointment (customerId, title, description, location, contact, url, start, end, createDate, createdBy, lastUpdate, lastUpdateBy) "
                                     + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)");
            ps.setInt(1, custID);
            ps.setString(2, title);
            ps.setString(3, desc);
            ps.setString(4, loc);
            ps.setString(5, contact);
            ps.setString(6, url);
            ps.setString(7, start);
            ps.setString(8, end);
            ps.setString(9, user);
            ps.setString(10, user);
            ps.execute();
            System.out.println("Appointment: " + title + " was submitted by User: " + user);
        }
        catch(SQLException err){
            
        }
        finally{
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}

