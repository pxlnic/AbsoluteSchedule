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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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

//Appt List handling
    //Load/Get all appointments
    public static ObservableList<Calendar> loadAppts() throws SQLException{
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
    
}
