/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import static absoluteschedule.Helper.SQLManage.getConn;
import absoluteschedule.Model.Customer;
import static absoluteschedule.View_Controller.LogInController.loggedOnUser;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author NicR
 */
public class CustomerViewController implements Initializable {

//FXML Declarations
    //Customer etnry fields
    @FXML private TextField CustomerIDField;
    @FXML private TextField CustomerNameField;
    @FXML private TextField CustomerPhoneNumberField;
    @FXML private TextField CustomerAddress1Field;
    @FXML private TextField CustomerAddress2Field;
    @FXML private TextField CustomerCityField;
    @FXML private TextField CustomerCountryField;
    @FXML private TextField CustomerPostalCodeField;
    @FXML private CheckBox CustomerActiveCheckbox;
    //Customer Table View
    @FXML private TableView<Customer> CustomerTableView;
    @FXML private TableColumn<Customer, Integer> CustomerID;
    @FXML private TableColumn<Customer, String> CustomerName;
    @FXML private TableColumn<Customer, String> CustomerAddress;
    @FXML private TableColumn<Customer, String> CustomerPostalCode;
    //Customer entry buttons
    @FXML private Button CustomerCancelButton;
    @FXML private Button CustomerClearButton;
    @FXML private Button CustomerSaveButton;
    //Header search field/buttons
    @FXML private TextField CustomerSearchField;
    @FXML private Button CustomerSearchButton;
    @FXML private Button CustomerSearchClearButton;
    
//Instance Variables
    //ObservableList to hold customer data for TableView
    private static ObservableList<Customer> customerList = FXCollections.observableArrayList();
    
    //SQL DB Variables
    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private ResourceBundle localization;
    
//Customer button handlers
    
//Cancel Button handler
    @FXML void CustomerCancelClick(ActionEvent event) throws IOException {
        System.out.println("Cancel clicked. Returning to main screen.");
        
    //Popup to confirm cancel
        
    //Load MainView scene
        Parent mainView = FXMLLoader.load(getClass().getResource("MainView.fxml"));
        Scene scene = new Scene(mainView);
        
    //Loads stage information from main file
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
    //Load scene onto stage
        window.setScene(scene);
        window.show();
        
    //Center Stage on middle of screen
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        window.setX((primScreenBounds.getWidth() - window.getWidth()) / 2);
        window.setY((primScreenBounds.getHeight() - window.getHeight()) / 2);
    }
//Clear Button handler
    @FXML void CustomerClearClick(ActionEvent event) {
        CustomerIDField.setText("");
        CustomerNameField.setText("");
        CustomerAddress1Field.setText("");
        CustomerAddress2Field.setText("");
        CustomerCityField.setText("");
        CustomerCountryField.setText("");
        CustomerPostalCodeField.setText("");
        CustomerPhoneNumberField.setText("");
        CustomerActiveCheckbox.setSelected(false);
    }
//Save Button handler
    @FXML void CustomerSaveClick(ActionEvent event) throws SQLException {
    //Variables
        int countryID = -1;
        int cityID = -1;
        int addressID = -1;
        int customerID = -1;
        Customer tempCust = new Customer();
        
    //Getting TextField entries
        tempCust.setCountry(CustomerCountryField.getText().trim());
        tempCust.setCity(CustomerCityField.getText().trim());
        tempCust.setAddress(CustomerAddress1Field.getText().trim(), CustomerAddress2Field.getText().trim());
        tempCust.setCustName(CustomerNameField.getText().trim());
        tempCust.setPhone(CustomerPhoneNumberField.getText().trim());
        tempCust.setPostalCode(CustomerPostalCodeField.getText().trim());
        if(CustomerActiveCheckbox.isSelected()){
           tempCust.setCustActive(1); 
        }
        else{
            tempCust.setCustActive(0);
        }
        
    //Validate Customer info - Table, Name Column, ID Column, Item Name
        //Validate Country ID
        countryID = tempCust.isIDValid("Country", "country", "countryId", tempCust.getCustCountry());
        //Validate City ID
        cityID = tempCust.isIDValid("City", "city", "cityId", tempCust.getCustCity());
        //Validate Address ID - Because of the two address fields this is a sepearte method
        addressID = tempCust.isAddressValid(tempCust.getCustAddress1(), tempCust.getCustAddress2());
        //Validate Customer ID
        customerID = tempCust.isIDValid("Customer", "customerName", "customerId", tempCust.getCustName());
        
        System.out.println("CountryID: " + countryID + ", CityID: " + cityID + ", AddressID: " + addressID + ", CustomerID: " + customerID);
        System.out.println("Submission was made by: " + loggedOnUser());

    //Run add customer methods
        if(countryID == -1){
            tempCust.addCountry(tempCust.getCustCountry(), loggedOnUser());
        }
                //Add Else Statement once update logic added
        if(cityID == -1){
            tempCust.addCity(tempCust.getCustCity(), tempCust.getCustCountry(), loggedOnUser());
        }
                //Add Else Statement once update logic added
        if(addressID == -1){
            tempCust.addAddress(tempCust.getCustAddress1(), tempCust.getCustAddress2(), tempCust.getCustCity(), tempCust.getCustPostalCode(), tempCust.getCustPhone(), loggedOnUser());
        }
                //Add Else Statement once update logic added
        if(customerID == -1){
            tempCust.addCustomer(tempCust.getCustName(), tempCust.getCustAddress1(), tempCust.getCustAddress2(), tempCust.getCustActive(), loggedOnUser());
        }
                //Add Else Statement once update logic added
        
        
    }
//Search Button handler
    @FXML void CustomerSearchClick(ActionEvent event) {

    }
//Clear Search Button handler
    @FXML void CustomerSearchClearClick(ActionEvent event) {
        CustomerSearchField.setText("");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadCustomers();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    //Populate TableView Data
        CustomerID.setCellValueFactory(cellData -> cellData.getValue().custIDProperty().asObject());
        CustomerName.setCellValueFactory(cellData -> cellData.getValue().custNameProperty());
        CustomerAddress.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().custAddress1Property()," ",cellData.getValue().custAddress2Property()));
        CustomerPostalCode.setCellValueFactory(cellData -> cellData.getValue().custPostalCodeProperty());
            
    //Load TableView
        updateCustomerTableView();
        
    //Enable TableView Selection to populate textfields
        CustomerTableView.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, selectedCust) -> {
            System.out.println("Customer: " + selectedCust.getCustName() + " was selected.");
        //Set textfield values
            String id = Integer.toString(selectedCust.getCustID());
            CustomerIDField.setText(id);
            CustomerNameField.setText(selectedCust.getCustName());
            CustomerAddress1Field.setText(selectedCust.getCustAddress1());
            CustomerAddress2Field.setText(selectedCust.getCustAddress2());
            CustomerCityField.setText(selectedCust.getCustCity());
            CustomerCountryField.setText(selectedCust.getCustCountry());
            CustomerPostalCodeField.setText(selectedCust.getCustPostalCode());
            CustomerPhoneNumberField.setText(selectedCust.getCustPhone());
            if(selectedCust.getCustActive()==1){
                CustomerActiveCheckbox.setSelected(true);
            }
            else{
                CustomerActiveCheckbox.setSelected(false);
            }
        });
    }
    
//Load TableView Data
    private void updateCustomerTableView(){
        CustomerTableView.setItems(customerList);
    }
    
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
                cust.setPhone("phone");
                cust.setCityID(rs.getInt("cityId"));
                cust.setCity("city");
                cust.setCountryID(rs.getInt("countryId"));
                cust.setCountry("country");
                customerList.add(cust);
                System.out.println("Customer: " + cust.getCustName() + " was added.");
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
        System.out.println("# of customers: " + customerList.size());
        return customerList;
    }
    
}
