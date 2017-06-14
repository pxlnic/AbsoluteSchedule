/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author NicR
 */
public class ReportViewController implements Initializable {

//FXML Declarations
    @FXML private ComboBox<String> ReportTypeCombo;
    @FXML private ComboBox<String> ReportMonthCombo;
    @FXML private ComboBox<Integer> ReportYearCombo;
    @FXML private TextField ReportExportTitleField;
    @FXML private TextArea ReportExportNotesField;
    @FXML private Button ReportCancelButton;
    @FXML private Button ReportClearButton;
    @FXML private Button ReportViewButton;
    @FXML private Label ReportNameLabel;
    @FXML private Button ReportSaveButton;
    @FXML private Label ReportDateLabel;
    @FXML private TableView<?> ReportTableView;

//FXML Button Handlers
    
//Cancel Button handler
    @FXML void ReportCancelClick(ActionEvent event) throws IOException {
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
    @FXML void ReportClearClick(ActionEvent event) {
        ReportTypeCombo.getItems().clear();
        ReportMonthCombo.getItems().clear();
        ReportYearCombo.getItems().clear();
        ReportExportTitleField.setText("");
        ReportExportNotesField.setText("");
        ReportNameLabel.setText("Report Name");
        ReportDateLabel.setText("Month - Year");
    }
//View/Generate Report Button handler
    @FXML void ReportViewClick(ActionEvent event) {
        ReportNameLabel.setText(ReportTypeCombo.getValue());
        ReportDateLabel.setText(ReportMonthCombo.getValue() + " - " + ReportYearCombo.getValue());
    }
//Save Button handler
    @FXML void ReportSaveClick(ActionEvent event) {

    }
    //
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Report Type Combo Options
        ReportTypeCombo.getItems().addAll("Monthly Appointment Count","Consultant Schedule", "Appointments By Day");
        
    //Month/Year Combo Options
        ReportMonthCombo.getItems().addAll("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        ReportYearCombo.getItems().addAll(2015, 2016, 2017, 2018, 2019, 2020);
    }    
    
}
