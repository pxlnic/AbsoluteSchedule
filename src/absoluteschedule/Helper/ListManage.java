/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Helper;

import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
import static absoluteschedule.Helper.SQLManage.getConn;
import absoluteschedule.Model.Calendar;
import absoluteschedule.Model.Customer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
    private static ObservableList<Calendar> calWeekList = FXCollections.observableArrayList();
    private static ObservableList<Calendar> calMonthList = FXCollections.observableArrayList();
    
//Instance Variables
    private ResourceBundle localization = loadResourceBundle();
    
//Customer DB/List Handling
    
//Load all customers
    public ObservableList<Customer> loadCustomers() throws SQLException{
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
        System.out.println("# of customers: " + custList.size());
    //Return list of customers
        return custList;
    } 

//Load all appointments
    public ObservableList<Calendar> loadAppts() throws SQLException{
        try(Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM appointment;")){
            
        //Execute Query
            ResultSet rs = ps.executeQuery();
            
        //Load customers
            while(rs.next()){
                Calendar cal = new Calendar();
                cal.setApptID(rs.getInt("appointmentId"));
                cal.setCustID(rs.getInt("customerId"));
                cal.setApptTitle(rs.getString("title"));
                cal.setApptDesc(rs.getString("description"));
                cal.setApptLoc(rs.getString("location"));
                cal.setApptContact(rs.getString("contact"));
                cal.setApptURL(rs.getString("url"));
                cal.setApptStartTime(rs.getString("start"));
                cal.setApptEndTime(rs.getString("end"));
                mainApptList.add(cal);
            }
        }
        catch(SQLException err){
            err.printStackTrace();
        }
        System.out.println("# of appointments: " + mainApptList.size());
        return mainApptList;
    }
    
}
