/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Model;

import java.util.Date;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author NicR
 */
public class Customer {
//Instance variables
    protected IntegerProperty custID;
    protected StringProperty custName;
    protected StringProperty custPhone;
    protected StringProperty custAddr1;
    protected StringProperty custAddr2;
    protected StringProperty custCity;
    protected StringProperty custPostalCode;
    protected StringProperty custCountry;
    protected IntegerProperty custActive;
    protected String custCreatedBy;
    protected Date custedCreated;
    protected String custUpdatedBy;
    protected Date custUpdated;
    
//Contructor
    public Customer(){
        custID = new SimpleIntegerProperty(-1);
        custName = new SimpleStringProperty("Change this name");
        custPhone = new SimpleStringProperty("Change this phone number");
        custAddr1 = new SimpleStringProperty("Change this address");
        custAddr2 = new SimpleStringProperty("Change this address");
        custCity = new SimpleStringProperty("Change this city");
        custPostalCode = new SimpleStringProperty("Change this postal code");
        custCountry = new SimpleStringProperty("Change this country");
        custActive = new SimpleIntegerProperty(0);
    }
    
//Getters and Setters for properties
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
    //Customer Name
    public void setName(String custName){
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
    public String getCustAddress(){
        return this.custAddr1.get()+ " " + this.custAddr2.get();
    }
    public StringProperty custAddressProperty(){
        StringProperty addr = null;
        addr.bind(Bindings.concat(custAddr1).concat(" ").concat(custAddr2));
        return addr; 
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
    public IntegerProperty getCustActive(){
        return custActive;
    }
}
