/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import absoluteschedule.Helper.ListManage;
import absoluteschedule.Model.Calendar;
import absoluteschedule.Model.Users;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    
//Intance Variables
    private List<Calendar> apptList = new ArrayList<>();
    private List<Users> consultantList = new ArrayList<>();
    private List<Integer> consultantApptCount = new ArrayList<>();
    private List<String> reportTypeList = new ArrayList<>();

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
        ReportTypeCombo.getSelectionModel().clearSelection();
        ReportMonthCombo.getSelectionModel().clearSelection();
        ReportYearCombo.getSelectionModel().clearSelection();
        ReportExportTitleField.setText("");
        ReportExportNotesField.setText("");
        ReportNameLabel.setText("Report Name");
        ReportDateLabel.setText("Month - Year");
    }
    //View/Generate Report Button handler
    @FXML void ReportViewClick(ActionEvent event) throws SQLException {
        
    //Set Header Labels
        ReportNameLabel.setText(ReportTypeCombo.getValue());
        ReportDateLabel.setText(ReportMonthCombo.getValue() + " - " + ReportYearCombo.getValue());
        
    //Load appointment data for selected month
        String dateText = ReportYearCombo.getValue() + "-" + ReportMonthCombo.getValue() + "-01";
        LocalDate selectedMonth = LocalDate.parse(dateText);
        System.out.println(selectedMonth);
        ListManage l = new ListManage();
        l.seperateAppts(selectedMonth);
        
        apptList = l.getMonthsAppts();
        
    //Determin which report to run
        switch(ReportTypeCombo.getValue()){
            case "Monthly Appointment Count":
                runMonthlyApptCount();
                break;
            case "Consultant Schedule":
                runConsultantApptCount();
                break;
            case "Appointments By Day":
                runDayCount();
                break;
            default:
                System.out.println("No Selection Made");
        }
        
    }
//Save Button handler
    @FXML void ReportSaveClick(ActionEvent event) {
        //Export data to csv/text file
    }
    
    
//Report Generation Methods
    //Monthly Appt Count Method
    public void runMonthlyApptCount() throws SQLException{
    //
        System.out.println("Monthly Count of appointments by type run");
    }
    //Appointments Counts by Consultant  for specified month
    public void runConsultantApptCount(){
        System.out.println("Monthly Count of appointments by consultnat run");
    }
    //Appt count by Day for specified month
    public void runDayCount(){
        System.out.println("Monthly Count of appointments by day run");
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Load Report Types
        reportTypeList.add("Monthly Appointment Count");
        reportTypeList.add("Consultant Schedule");
        reportTypeList.add("Appointments By Day");
        
    //Report Type Combo Options
        ReportTypeCombo.getItems().addAll(reportTypeList);
        
    //Month/Year Combo Options
        ReportMonthCombo.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        ReportYearCombo.getItems().addAll(2015, 2016, 2017, 2018, 2019, 2020);
    }
}
