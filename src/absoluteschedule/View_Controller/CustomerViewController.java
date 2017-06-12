/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import absoluteschedule.Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import static javafx.collections.FXCollections.observableList;
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
    
//Customer button handlers
    
//Cancel Button handler
    @FXML void CustomerCancelClick(ActionEvent event) throws IOException {
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
//Clear Button handler
    @FXML void CustomerClearClick(ActionEvent event) {
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
    @FXML void CustomerSaveClick(ActionEvent event) {

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
    //Populate TableView Data
        CustomerID.setCellValueFactory(cellData -> cellData.getValue().custIDProperty().asObject());
    }    
    
}
