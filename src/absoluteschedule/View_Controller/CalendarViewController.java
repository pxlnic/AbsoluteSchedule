/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author NicR
 */
public class CalendarViewController implements Initializable {

//FXML Declarations
    //Appointment entry items
    @FXML private DatePicker CalendarDatePicker;
    @FXML private ComboBox<Integer> CalendarTimeHourCombo;
    @FXML private ComboBox<Integer> CalendarTimeMinCombo;
    @FXML private ComboBox<Integer> CalendarDurationCombo;
    @FXML private CheckBox CalendaryAllDayCheckbox;
    @FXML private ComboBox<String> CalendarCustomerCombo;
    @FXML private ComboBox<String> CalendarLocationCombo;
    @FXML private TextField CalendarTitleField;
    @FXML private TextArea CalendarDescriptionArea;
    @FXML private Button CalendarCancelButton;
    @FXML private Button CalendarClearButton;
    @FXML private Button CalendarSaveButton;
    
    //Week Tab
    
    //Month Tab
    @FXML private Button CalendarMonthBackButton;
    @FXML private Label CalendarMonthTabHeader;
    @FXML private Button CalendarMonthNextButton;
    @FXML private GridPane CalendarMonthGrid;
    
//Instance Variables

    
//Footer Button handlers
    //Cancel Button handler
    @FXML void CalendarCancelClick(ActionEvent event) throws IOException {
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
    @FXML void CalendarClearClick(ActionEvent event) {
        CalendarDatePicker.setValue(LocalDate.now());
        CalendarTimeHourCombo.getItems().clear();
        CalendarTimeMinCombo.getItems().clear();
        CalendarDurationCombo.getItems().clear();
        CalendarCustomerCombo.getItems().clear();
        CalendarLocationCombo.getItems().clear();
        CalendaryAllDayCheckbox.setSelected(false);
        CalendarTitleField.setText("");
        CalendarDescriptionArea.setText("");
    }
    //Save Button handler
    @FXML void CalendarSaveClick(ActionEvent event) {

    }
    //Month Back Button handler
    @FXML void CalendarMonthBackClick(ActionEvent event) {

    }
    //Month Next Button handler
    @FXML void CalendarMonthNextClick(ActionEvent event) {

    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Populate Datepicker
        CalendarDatePicker.setValue(LocalDate.now());
        
    //Populate Hour/Min Combo Boxes
        CalendarTimeHourCombo.getItems().addAll(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23);
        CalendarTimeMinCombo.getItems().addAll(0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55);
        
    //Populate Duration Combo Box
        CalendarDurationCombo.getItems().addAll(15, 30, 45, 60, 90, 120, 180, 240, 360, 480);
        
    //Populate Customer Combo Box
        CalendarCustomerCombo.getItems().addAll("Name1", "Name2", "Name3", "Name4", "Name5");
        
    //Populate Location Combo Box
        CalendarLocationCombo.getItems().addAll("Location1", "Location2", "Location3");
    }    
    
}
