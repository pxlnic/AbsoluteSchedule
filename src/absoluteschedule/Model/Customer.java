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
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import static jdk.nashorn.internal.objects.NativeError.printStackTrace;

/**
 *
 * @author NicR
 */
public class Customer {
//Instance variables
    //Customer value variables
    protected IntegerProperty custID;
    protected StringProperty custName;
    protected StringProperty custPhone;
    protected int custAddrID;
    protected StringProperty custAddr1;
    protected StringProperty custAddr2;
    protected StringProperty custPostalCode;
    protected int custCityID;
    protected StringProperty custCity;
    protected int custCountryID;
    protected StringProperty custCountry;
    protected IntegerProperty custActive;
    
    //SQL Variables
    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private ResourceBundle localization;
    
//Contructor
    public Customer(){
        custID = new SimpleIntegerProperty(-1);
        custName = new SimpleStringProperty("Change this name");
        custPhone = new SimpleStringProperty("Change this phone number");
        custAddrID = -1;
        custAddr1 = new SimpleStringProperty("Change this address");
        custAddr2 = new SimpleStringProperty("Change this address");
        custPostalCode = new SimpleStringProperty("Change this postal code");
        custCityID = -1;
        custCity = new SimpleStringProperty("Change this city");
        custCountryID = -1;
        custCountry = new SimpleStringProperty("Change this country");
        custActive = new SimpleIntegerProperty(0);
    }
    
//Getters and Setters for properties
    //Customer Information ID's
    //Customer ID
    public void setCustID(int custID){
        this.custID.set(custID);
    }
    public int getCustID(){
        return this.custID.get();
    }
    public IntegerProperty custIDProperty(){
        return custID;
    }
    //Address ID
    public void setAddrID(int addrID){
        custAddrID = addrID;
    }
    public int getAddrID(){
        return custAddrID;
    }
    //City ID
    public void setCityID(int cityID){
        custCityID = cityID;
    }
    public int getCityID(){
        return custCityID;
    }
    //Country ID
    public void setCountryID(int countryID){
        custCountryID = countryID;
    }
    public int getCountryID(){
        return custCountryID;
    }
    //Customer Name
    public void setCustName(String custName){
        this.custName.set(custName);
    }
    public String getCustName(){
        return this.custName.get();
    }
    public StringProperty custNameProperty(){
        return custName;
    }
    //Customer Phone Number
    public void setPhone(String custPhone){
        this.custPhone.set(custPhone);
    }
    public String getCustPhone(){
        return this.custPhone.get();
    }
    public StringProperty custPhoneProperty(){
        return custPhone;
    }
    //Customer Address
    public void setAddress(String custAddress1, String custAddress2){
        this.custAddr1.set(custAddress1);
        this.custAddr2.set(custAddress2);
    }
    public String getCustAddress1(){
        return this.custAddr1.get();
    }
    public String getCustAddress2(){
        return this.custAddr2.get();
    }
    public StringProperty custAddress1Property(){
        return custAddr1; 
    }
    public StringProperty custAddress2Property(){
        return custAddr2;
    }
    //Customer City
    public void setCity(String custCity){
        this.custCity.set(custCity);
    }
    public String getCustCity(){
        return this.custCity.get();
    }
    public StringProperty custCityProperty(){
        return custCity; 
    }
    //Customer Postal Code
    public void setPostalCode(String custPostalCode){
        this.custPostalCode.set(custPostalCode);
    }
    public String getCustPostalCode(){
        return this.custPostalCode.get();
    }
    public StringProperty custPostalCodeProperty(){
        return custPostalCode;
    }
    //Customer Country
    public void setCountry(String custCountry){
        this.custCountry.set(custCountry);
    }
    public String getCustCountry(){
        return this.custCountry.get();
    }
    public StringProperty custCountryProperty(){
        return custCountry;
    }
    //Custoemr Active
    public void setCustActive(Integer custActive){
        this.custActive.set(custActive);
    }
    public int getCustActive(){
        return this.custActive.get();
    }
    
//Add New Customer Methods
    //Add Country
    public void addCountry(String countryName, String user) throws SQLException{
        try{
        //Set SQL based variables
            conn = getConn();
            
        //Based on ID values perform update or add to DB
            //Add country
            
            ps = conn.prepareStatement("INSERT INTO country (country, createDate, createdBy, lastUpdate, lastUpdateBy) "
                                     + "VALUES (?, NOW(), ?, NOW(), ?)");
            ps.setString(1, countryName);
            ps.setString(2, user);
            ps.setString(3, user);
            ps.execute();
            System.out.println("Country: " + countryName + " was submitted by User: " + user);
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
    //Add City
    public void addCity(String cityName, String countryName, String user) throws SQLException{
        try{
        //Set SQL based variables
            conn = getConn();
            int countryID = -1;
            
        //Gets countryID
            ps = conn.prepareStatement("SELECT * FROM country WHERE country=?");
            ps.setString(1, countryName);
            rs = ps.executeQuery();
                while(rs.next()){
                    countryID = rs.getInt("countryId");
                }
            
        //Add country
            ps = conn.prepareStatement("INSERT INTO city (city, countryId, createDate, createdBy, lastUpdate, lastUpdateBy) "
                                     + "VALUES (?, ?, NOW(), ?, NOW(), ?)");
            ps.setString(1, cityName);
            ps.setInt(2, countryID);
            ps.setString(3, user);
            ps.setString(4, user);
            ps.execute();
            System.out.println("City: " + cityName + " was submitted by User: " + user);
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
    //Add Address
    public void addAddress(String address1, String address2, String cityName, String postalCode, String phoneNum, String user) throws SQLException{
        try{
        //Set SQL based variables
            conn = getConn();
            int cityID = -1;
            
        //Gets countryID
            ps = conn.prepareStatement("SELECT * FROM city WHERE city=?");
            ps.setString(1, cityName);
            rs = ps.executeQuery();
                while(rs.next()){
                    cityID = rs.getInt("cityId");
                }
        //Add country
            ps = conn.prepareStatement("INSERT INTO address (address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
                                     + "VALUES (?, ?, ?, ?, ?, NOW(), ?, NOW(), ?)");
            ps.setString(1, address1);
            ps.setString(2, address2);
            ps.setInt(3, cityID);
            ps.setString(4, postalCode);
            ps.setString(5, phoneNum);
            ps.setString(6, user);
            ps.setString(7, user);
            ps.execute();
            System.out.println("Address: " + address1 + " " + address2 + " was submitted by User: " + user);
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
    //Add Customer
    public void addCustomer(String custName, String address1, String address2, int active, String user) throws SQLException{
        try{
        //Set SQL based variables
            conn = getConn();
            int addressID = -1;
            
        //Gets countryID
            ps = conn.prepareStatement("SELECT * FROM address WHERE address=? AND address2=?");
            ps.setString(1, address1);
            ps.setString(2, address2);
            rs = ps.executeQuery();
                while(rs.next()){
                    addressID = rs.getInt("addressId");
                }
            
        //Add country
            ps = conn.prepareStatement("INSERT INTO customer (customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
                                     + "VALUES (?, ?, ?, NOW(), ?, NOW(), ?)");
            ps.setString(1, custName);
            ps.setInt(2, addressID);
            ps.setInt(3, active);
            ps.setString(4, user);
            ps.setString(5, user);
            ps.execute();
            System.out.println("Customer: " + custName + " was submitted by User: " + user);
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
    
//Update Existing Customer Methods
    //Update Country
    public void updateCountry(Integer countryID, String countryName, String user) throws SQLException{
        try{
            conn = getConn();
            ps = conn.prepareStatement("UPDATE country SET country=?, lastUpdate=NOW(), lastUpdatedBy=? WHERE countryId=?");
            ps.setString(1, countryName);
            ps.setString(2, user);
            ps.setInt(3, countryID);
            ps.executeUpdate();
            
        }
        catch(SQLException err){
            
        }
        finally{
            
        }
    }
    //Update City
    
    //Update Address
    
    //Update Customer
    
//Pull customer data for update (from observable arraylist)
    
    
//Validate customer info with database to see if needs to added or if exists 
    //Check Country - (Add to SQL DB if does not exist and return ID to add to city)
    public int isIDValid(String itemTable, String itemCol, String itemIDCol, String itemName) throws SQLException{
        System.out.println("Checking if "+ itemTable + " exists in DB.");
    //Create method variables
        int dbItemID = 0;
        String dbItemName = "";
        int passedID = 0;
        int count = 0;
        
    //Try clause
        try{
        //Open Connection
            conn = getConn();
            
        //Create and Execute Statement
            ps = conn.prepareStatement("SELECT * FROM " + itemTable.toLowerCase() + " WHERE " + itemCol + "=?");
            ps.setString(1, itemName);
            rs = ps.executeQuery();

        //Set variables with data from DB
            if(rs.next()){
                System.out.println("Setting variables from SQL Query");
                dbItemName = rs.getString(itemCol);
                dbItemID = rs.getInt(itemIDCol);
            }
            else{
                System.out.println("Setting variables to test logic");
                dbItemName = "No " + itemTable;
                dbItemID = -1;
            }
            System.out.println(itemTable + ": " + dbItemName + ", " + itemTable + " ID: " + dbItemID);
            
            passedID = dbItemID;
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
              
    //Return ID. If no match then returns -1
        System.out.println("Passed ID: " + passedID);
        return passedID;
    }

    //Check Address - (Add to SQL DB if does not exist and return ID to add to customer)
    public int isAddressValid(String addr1, String addr2) throws SQLException{
        System.out.println("Checking if address exists in DB.");
    //Create method variables
        int dbAddrID = 0;
        String dbAddr = "";
        int passedID = 0;
        int count = 0;
        
        String addrText = "address";
        
    //Try clause
        try{
        //Open Connection
            conn = getConn();
            
        //Create and Execute Statement
            ps = conn.prepareStatement("SELECT * FROM address WHERE address=? AND address2=?");
            ps.setString(1, addr1);
            ps.setString(2, addr2);
            rs = ps.executeQuery();

        //Set variables with data from DB
            if(rs.next()){
                System.out.println("Setting variables from SQL Query");
                dbAddr = rs.getString(addrText) + " " + rs.getString("address2");
                dbAddrID = rs.getInt("addressId");
            }
            else{
                System.out.println("Setting variables to test logic");
                dbAddr = "No Address";
                dbAddrID = -1;
            }
            System.out.println("Address: " + dbAddr + ", Address ID: " + dbAddrID);
            
            passedID = dbAddrID;
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
              
    //Return ID. If no match then returns -1
        System.out.println("Passed ID: " + passedID);
        return passedID;
    }    

}
