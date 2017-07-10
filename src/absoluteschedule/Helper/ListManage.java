/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Helper;

import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
import static absoluteschedule.Helper.SQLManage.getConn;
import absoluteschedule.Model.Calendar;
import static absoluteschedule.Model.Calendar.convertToLocal;
import absoluteschedule.Model.Customer;
import absoluteschedule.Model.Report;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;

/**
 *
 * @author NicR
 */
public class ListManage {
//Observable Lists
    private static ObservableList<Customer> custList = FXCollections.observableArrayList();
    private static ObservableList<Calendar> mainApptList = FXCollections.observableArrayList();
    private static ObservableList<Calendar> calTodayList = FXCollections.observableArrayList();
    private static ObservableList<Calendar> calWeekList = FXCollections.observableArrayList();
    private static ObservableList<Calendar> calMonthList = FXCollections.observableArrayList();
    private static List<Report> mainReportList = new ArrayList<>();
    private static List<String> mainConsultantList = new ArrayList<>();
    
//Instance Variables
    private ResourceBundle localization = loadResourceBundle();
    
//Customer DB/List Handling
    //Load all customers
    public static ObservableList<Customer> loadCustomers() throws SQLException{
        try(Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT customer.customerId, customer.customerName, customer.addressId, customer.active, address.addressId, address.address, address.address2, address.cityId, address.postalCode, address.phone, city.cityId, city.city, city.countryId, country.countryId, country.country\n" +
                                                         "FROM customer\n" +
                                                         "LEFT JOIN address on customer.addressId = address.addressId\n" +
                                                         "LEFT JOIN city on address.cityId = city.cityId\n" +
                                                         "LEFT JOIN country on city.countryId = country.countryId;")){
        //Execute Query
            ResultSet rs = ps.executeQuery();
            
            System.out.println("Loading Customers");

        //Load Customers
            while(rs.next()){
                Customer cust = new Customer();
                cust.setCustID(rs.getInt("customerId"));
                cust.setCustName(rs.getString("customerName"));
                cust.setCustActive(rs.getInt("active"));
                cust.setAddrID(rs.getInt("addressId"));
                cust.setAddress(rs.getString("address"), rs.getString("address2"));
                cust.setPostalCode(rs.getString("postalCode"));
                cust.setPhone(rs.getString("phone"));
                cust.setCityID(rs.getInt("cityId"));
                cust.setCity(rs.getString("city"));
                cust.setCountryID(rs.getInt("countryId"));
                cust.setCountry(rs.getString("country"));
                custList.add(cust);
            }
        }
        catch(SQLException err){
            err.printStackTrace();
        }
        System.out.println("# of customers retrieved from DB: " + custList.size());
    //Return list of customers
        return custList;
    } 
    //Load all consultants
    public static List<String> loadConsultList() throws SQLException {
        try (Connection conn = getConn();
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM user")) {;
                ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String user = rs.getString("userName");
                if(mainConsultantList.contains(user)){
                }
                else{
                    mainConsultantList.add(user);
                }
            }
        } catch (SQLException err) {
            err.printStackTrace();
        }
        System.out.println("Consultant Count: " + mainConsultantList.size());
        return mainConsultantList;
    }
    //Customer lookup
    public static ObservableList lookupCust(String input, ObservableList<Customer> custList ){
        ObservableList<Customer> tempList = FXCollections.observableArrayList();
        
    //Test if input is number or letter
        if(isInteger(input)){
        //If number
            int id = Integer.parseInt(input);
            for(int i=0; i<custList.size(); i++){
                List<Integer> validId = new ArrayList<>();
                validId.add(custList.get(i).getCustID());
                if(validId.get(0)==id){
                    tempList.add(custList.get(i));
                }
            }
        }
        else{
        //If letter
            for(int i=0; i<custList.size(); i++){
                List<String> validName = new ArrayList<>();
                validName.add(custList.get(i).getCustName());
                if(validName.get(0).contains(input)){
                    tempList.add(custList.get(i));
                }
            }
            System.out.println("Count of partial/match: " + tempList.size());
        }
        return tempList;
    }

//Appt List handling
    //Load/Get all appointments
    public static ObservableList<Calendar> loadAppts() throws SQLException{
        
        mainApptList.clear();
        
        try(Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointment ORDER BY start;")){
            
        //Execute Query
            ResultSet rs = ps.executeQuery();
            
        //Load customers
            while(rs.next()){
            //Convert Start/End dates to local time
                String startString = rs.getString("start");
                String endString = rs.getString("end");
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
                LocalDateTime localStartTime = LocalDateTime.parse(startString, formatter);
                LocalDateTime localEndTime = LocalDateTime.parse(endString, formatter);
                
                String finalStart = convertToLocal(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localStartTime));
                String finalEnd = convertToLocal(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(localEndTime));

                
                Calendar cal = new Calendar();
                cal.setApptID(rs.getInt("appointmentId"));
                cal.setCustID(rs.getInt("customerId"));
                cal.setApptTitle(rs.getString("title"));
                cal.setApptDesc(rs.getString("description"));
                cal.setApptLoc(rs.getString("location"));
                cal.setApptContact(rs.getString("contact"));
                cal.setApptURL(rs.getString("url"));
                cal.setApptStartTime(finalStart);
                cal.setApptEndTime(finalEnd);
                mainApptList.add(cal);
            }
        }
        catch(SQLException err){
            err.printStackTrace();
        }
        System.out.println("# of appointments: " + mainApptList.size());
        return mainApptList;
    }
    //Load Today's, Week's, and Month's Appointments
    public void seperateAppts(LocalDate inputDate) throws SQLException{
    //Clear Appt Lists
        mainApptList.clear();
        calTodayList.clear();
        calWeekList.clear();
        calMonthList.clear();
    
    //Load/Reload Main Appts List    
        loadAppts();
        
    //Seperate Appts
        LocalDate dateRef = inputDate;
        
        for(int i = 0; i < mainApptList.size(); i++){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = mainApptList.get(i).getApptStartTime();
            String subDate = dateString.substring(0,10);
            String subToday = formatter.format(dateRef);
            LocalDate date = LocalDate.parse(subDate, formatter);
            LocalDate todayFormatted = LocalDate.parse(subToday, formatter);
            if(date.equals(todayFormatted)){
                calTodayList.add(mainApptList.get(i));
            }
            if(date.isAfter(todayFormatted.with(DayOfWeek.MONDAY).minusDays(1)) && date.isBefore(todayFormatted.with(DayOfWeek.FRIDAY).plusDays(1))){
                calWeekList.add(mainApptList.get(i));
            }
            if(date.isAfter(todayFormatted.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1)) && date.isBefore(todayFormatted.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1))){
                calMonthList.add(mainApptList.get(i));
            }
        }
        System.out.println("Today: " + calTodayList.size() + ", This Week: " + calWeekList.size() + ", This Month: " + calMonthList.size());
    }
    //Return Today's, Week's, and Month's Appointments
    public static List<Calendar> getTodaysAppts(){
        return calTodayList;
    }
    public static List<Calendar> getWeeksAppts(){
        return calWeekList;
    }
    public static List<Calendar> getMonthsAppts(){
        return calMonthList;
    }
    
//Report List handling
    //Get Report List
    public static List<Report> getReportList(){
        return mainReportList;
    }
    
//Reminders
    public static void checkReminder(){
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), ev -> {
            try {
                loadAppts();
            } catch (SQLException ex) {
                Logger.getLogger(ListManage.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        //Temp List
            List<Calendar> tempList = new ArrayList<>();
            
        //Loop through appt list to see if any are within the next 15 minutes
            for(int i=0; i<mainApptList.size(); i++){
                Calendar item = mainApptList.get(i);
                String reminder = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime time = LocalDateTime.parse(item.getApptStartTime(), formatter);
                LocalDateTime now = LocalDateTime.parse(reminder, formatter);;
                if(time.isAfter(now) && time.isBefore(now.plusMinutes(15))){
                    tempList.add(item);
                }
            }
            
        //Print appointments if there are any
            if(tempList.isEmpty()){
                System.out.println("No Upcoming Appointments!");
            }
            else if(tempList.size()==1){
                System.out.println("Upcoming Appointments: ");
                System.out.println(tempList.get(0).getApptStartTime() + " - " + tempList.get(0).getApptContact());
            }
            else{
                System.out.println("Upcoming Events: ");
                System.out.println("Count: " + tempList.size());
            }
            
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play(); 
    }
    
//Test if entry is number or not
    public static boolean isInteger(String input) {
        try { //Try to make the input into an integer
            Integer.parseInt( input );
            return true; //Return true if it works
        }
        catch( Exception e ) { 
            return false; //If it doesn't work return false
        }
    }
}
