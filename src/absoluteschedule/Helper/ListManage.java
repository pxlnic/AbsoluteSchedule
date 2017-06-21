/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Helper;

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
    private static ObservableList<Calendar> mainAgendaList = FXCollections.observableArrayList();
    private static ObservableList<Calendar> calWeekList = FXCollections.observableArrayList();
    private static ObservableList<Calendar> calMonthList = FXCollections.observableArrayList();
    
//Instance Variables
    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private ResourceBundle localization;
    
//Customer DB/List Handling
    
//Load all customers
   public ObservableList<Customer> loadCustomers() throws SQLException{
        try{

        //Open connection
            conn = getConn();

        //Prepare statement to pull customer data and loop through to add to oberservable list
            ps = conn.prepareStatement("SELECT customer.customerId, customer.customerName, customer.addressId, customer.active, address.addressId, address.address, address.address2, address.cityId, address.postalCode, address.phone, city.cityId, city.city, city.countryId, country.countryId, country.country\n" +
                                       "FROM customer\n" +
                                       "LEFT JOIN address on customer.addressId = address.addressId\n" +
                                       "LEFT JOIN city on address.cityId = city.cityId\n" +
                                       "LEFT JOIN country on city.countryId = country.countryId;");
            rs = ps.executeQuery();
            
            System.out.println("Loading Customers");

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
                System.out.println("Customer: " + cust.getCustName() + " was added.");
                System.out.println("Phone: " + cust.getCustPhone() + ", City: " + cust.getCustCity() + ", Country: " + cust.getCustCountry());
            }
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
        System.out.println("# of customers: " + custList.size());
        return custList;
    }
    
    public ObservableList<Customer> getCustList() throws SQLException{
        loadCustomers();
        return custList;
    }
    
}
