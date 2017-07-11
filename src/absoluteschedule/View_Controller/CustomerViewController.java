/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import static absoluteschedule.AbsoluteSchedule.createConfirmAlert;
import static absoluteschedule.AbsoluteSchedule.createStandardAlert;
import static absoluteschedule.Helper.ListManage.loadCustomers;
import static absoluteschedule.Helper.ListManage.lookupCust;
import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
import absoluteschedule.Model.Customer;
import static absoluteschedule.View_Controller.LogInController.loggedOnUser;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
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
    String exceptionMessage = new String();
    
    //Variables to hold onto selected customer for editing
    private int prevCustID;
    private int prevCustAddrID;
    private int prevCustActive;
    private String prevCustPhone;
    private String prevCustPostalCode;
    private String prevCustCity;
    
    //SQL DB Variables
    private ResultSet rs = null;
    private ResourceBundle localization = loadResourceBundle();
    
//Customer button handlers 
    //Cancel Button handler
    @FXML void CustomerCancelClick(ActionEvent event) throws IOException {
        Optional<ButtonType> confirm = createConfirmAlert(localization.getString("cancel_confirm"), "Cancel Confirmation!", "Confirm!");
        
        if(confirm.get() == ButtonType.OK){
            System.out.println("Cancel clicked. Returning to main screen.");
        
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
        else{
            System.out.println("You clicked cancel. Please continue.");
        }
    }
    //Clear Button handler
    @FXML void CustomerClearClick(ActionEvent event) {
        Optional<ButtonType> confirm = createConfirmAlert(localization.getString("clear_confirm"), "Clear Fields?", "Confirm!");
        
        if(confirm.get() == ButtonType.OK){
            clearFields();
        }
        else{
            System.out.println("You clicked cancel. Please continue.");
        }
    }
    //Save Button handler
    @FXML void CustomerSaveClick(ActionEvent event) throws SQLException {
    //Variables
        int countryID = -1;
        int cityID = -1;
        int addressID = -1;
        int customerID = -1;
        Customer tempCust = new Customer();
        String user = loggedOnUser();
        
    //Reset Exception Message
        exceptionMessage = "";
        
    //Get Customer Data
        String name = CustomerNameField.getText().trim();
        String phone = CustomerPhoneNumberField.getText().trim();
        String address1 = CustomerAddress1Field.getText().trim();
        String address2 = CustomerAddress2Field.getText().trim();
        String city = CustomerCityField.getText().trim();
        String postal = CustomerPostalCodeField.getText().trim();
        String country = CustomerCountryField.getText().trim();
        Boolean active = CustomerActiveCheckbox.isSelected();
        
        try{
            //Test entry fields
            exceptionMessage = Customer.isEntryValid(exceptionMessage, name, phone, address1, city, postal, country);

            if(exceptionMessage.length()>0){
                createStandardAlert(exceptionMessage, "Not all fields complete!", "Empty Fields!");
            }
            else{
            //Getting TextField entries for customer
                tempCust.setCountry(country);
                tempCust.setCity(city);
                tempCust.setAddress(address1, address2);
                tempCust.setCustName(name);
                tempCust.setPhone(phone);
                tempCust.setPostalCode(postal);
                if(active){
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
                System.out.println("Submission was made by: " + user);

            //Run add/update customer methods for respective tables
                //Add country
                if(countryID == -1){
                    tempCust.addCountry(tempCust.getCustCountry(), user);
                }
                else{
                    System.out.println("Country already exists in DB.");
                }
                //Add city
                if(cityID == -1){
                    tempCust.addCity(tempCust.getCustCity(), tempCust.getCustCountry(), user);
                }
                else{
                    System.out.println("City already exists in DB.");
                }
                //Add/Update address
                if(addressID == -1 && CustomerIDField.getText().trim().isEmpty()){
                    tempCust.addAddress(tempCust.getCustAddress1(), tempCust.getCustAddress2(), tempCust.getCustCity(), tempCust.getCustPostalCode(), tempCust.getCustPhone(), user);
                }
                else{
                    System.out.println("Address info updated for this customer.");
                    tempCust.updateAddress(prevCustAddrID, tempCust.getCustAddress1(), tempCust.getCustAddress2(), tempCust.getCustCity(), tempCust.getCustPostalCode(), tempCust.getCustPhone(), user);
                }
                //Add/Update customer
                if(customerID == -1 && (CustomerIDField.getText().trim().isEmpty())){
                    tempCust.addCustomer(tempCust.getCustName(), tempCust.getCustAddress1(), tempCust.getCustAddress2(), tempCust.getCustActive(), user);
                }
                else{
                    System.out.println("Customer info updated for this customer.");
                    tempCust.updateCust(prevCustID, tempCust.getCustName(), tempCust.getCustAddress1(), tempCust.getCustAddress2(), tempCust.getCustActive(), user);
                }

            //Refresh Customer List
                reloadCustomers();
                
            //Clear fields
                clearFields();
            }
        }
        catch(NumberFormatException e){
            
        }
    }
    //Search Button handler
    @FXML void CustomerSearchClick(ActionEvent event) {
    //Clear Exception Message
        exceptionMessage = "";
        
    //Get customer text & create index variable
        String searchCust = CustomerSearchField.getText().trim();
        
    //Lookup Customer for partial/match
        ObservableList<Customer> tempList = lookupCust(searchCust, customerList);
        
    //If list is empty then throw error
        if(tempList.size()==0){
            exceptionMessage = exceptionMessage + "-Could not find customer based on search input.";
        }
        else{   
        //Set table data
            CustomerTableView.setItems(tempList);  
        }
    }
    //Clear Search Button handler
    @FXML void CustomerSearchClearClick(ActionEvent event) {
        CustomerSearchField.setText("");
        CustomerTableView.setItems(customerList);
    }   

//Clear Fields
    private void clearFields(){
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
//Load/Reload customer data and tableview
    private void reloadCustomers() throws SQLException{
        System.out.println("Customer List Loading.");

    //Clear Fields
        clearFields();
        
    //Clear and reload customer list
        customerList.clear();
        customerList = loadCustomers();
        //System.out.println("There are " + customerList.size() + " customers in the list.");
        
    //Populate TableView Data
        CustomerID.setCellValueFactory(cellData -> cellData.getValue().custIDProperty().asObject());
        CustomerName.setCellValueFactory(cellData -> cellData.getValue().custNameProperty());
        CustomerAddress.setCellValueFactory(cellData -> Bindings.concat(cellData.getValue().custAddress1Property()," ",cellData.getValue().custAddress2Property()));
        CustomerPostalCode.setCellValueFactory(cellData -> cellData.getValue().custPostalCodeProperty());
            
    //Load TableView
        System.out.println("TableView being set with " + customerList.size() + " customers.");
        CustomerTableView.setItems(customerList);
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Customer View Loaded!");
        try {
            reloadCustomers();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        CustomerTableView.refresh();
        CustomerTableView.requestFocus();
        CustomerTableView.getSelectionModel().select(0);
        CustomerTableView.getFocusModel().focus(0);
        
    //Enable TableView Selection to populate textfields
        CustomerTableView.getSelectionModel().selectedItemProperty().addListener((Observable, oldValue, selectedCust) -> {
            if(CustomerTableView.isFocused() == true){
                System.out.println("Customer: " + selectedCust.getCustName() + " was selected.");
                System.out.println("City: " + selectedCust.getCustCity() + ", Phone: " + selectedCust.getCustPhone() + ", Country: " + selectedCust.getCustCountry());
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
                prevCustID = selectedCust.getCustID();
                prevCustAddrID = selectedCust.getAddrID();
                prevCustActive = selectedCust.getCustActive();
                prevCustPhone = selectedCust.getCustPhone();
                prevCustPostalCode = selectedCust.getCustPostalCode();
                prevCustCity = selectedCust.getCustCity();
            }
        });
    }

}
