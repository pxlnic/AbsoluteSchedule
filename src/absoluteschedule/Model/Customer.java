/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Model;

import java.util.Date;
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
        custAddr1 = new SimpleStringProperty("Change this address");
        custAddr2 = new SimpleStringProperty("Change this address");
        custCity = new SimpleStringProperty("Change this city");
        custPostalCode = new SimpleStringProperty("Change this postal code");
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
    
}
