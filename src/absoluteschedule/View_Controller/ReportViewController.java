/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import absoluteschedule.Helper.ListManage;
import static absoluteschedule.Helper.ListManage.loadConsultList;
import absoluteschedule.Model.Calendar;
import absoluteschedule.Model.Report;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
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
    @FXML private TableView<Report> ReportTableView;
    
//Intance Variables
    private List<Calendar> apptList = new ArrayList<>();
    private List<String> reportTypeList = new ArrayList<>();
    private List<String> reportConsultantList = new ArrayList<>();
    private List<String> reportDayList = new ArrayList<>();
    private ObservableList<Report> reportList = FXCollections.observableArrayList();
    private String reportMonth = "";

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
        System.out.println("List size: " + reportList.size());
        reportList.clear();
        System.out.println("List size: " + reportList.size());
        
    //Set Header Labels
        reportMonth = ReportYearCombo.getValue() + "-" + ReportMonthCombo.getValue();
        ReportNameLabel.setText(ReportTypeCombo.getValue());
        ReportDateLabel.setText(reportMonth);
        
    //Load appointment data for selected month
        String dateText = ReportYearCombo.getValue() + "-" + ReportMonthCombo.getValue() + "-01";
        LocalDate selectedMonth = LocalDate.parse(dateText);
        System.out.println(selectedMonth);
        ListManage l = new ListManage();
        l.seperateAppts(selectedMonth);
        
        apptList = l.getMonthsAppts();
        
    //Determin which report to run
        switch(ReportTypeCombo.getValue()){
            case "Appointment Type":
                runApptTypeCount();
                break;
            case "Consultant Schedule":
                runConsultantAppt();
                break;
            case "Appointments By Day":
                runDayCount();
                break;
            default:
                System.out.println("No Selection Made");
        }
        
    //Add tabe columns and load date from reportlist
        loadTable();
    }
//Save Button handler
    @FXML void ReportSaveClick(ActionEvent event) {
        //Export data to csv/text file
    }
    
    
//Report Generation Methods
    //Type of Appt Count Method
    public void runApptTypeCount() throws SQLException{
        System.out.println("Monthly Count of appointments by type run");
        
    //Set Appt Headers as first item in reportList
        Report header = new Report();
        header.setReportName("Report Type");
        header.setReportTimePeriod("Month/Year");
        header.setReportType("Appt Type");
        header.setReportItem("Appt Count");
        reportList.add(header);
        
    //Run through appt list and match types and count        
        for(int i=0; i<reportTypeList.size(); i++){
        //Create new report object
            Report report = new Report();
            
        //Set initial report parameters
            report.setReportName("Appointment Type");
            report.setReportTimePeriod(reportMonth);
            report.setReportType(reportTypeList.get(i));
            
            int count = 0;
            for(int j=0; j<apptList.size();j++){
                if(apptList.get(j).getApptDesc().equals(reportTypeList.get(i))){
                    count++;
                }
            }
        //Set Report count
            report.setReportItem(count + "");
            
        //Add Report item to report list
            reportList.add(report);
        }
        System.out.println("Report List Size: " + reportList.size());
    }
    //Appointments Counts by Consultant  for specified month
    public void runConsultantAppt(){
        System.out.println("Monthly Count of appointments by consultnat run");
        
    //Set Appt Headers as first item in reportList
        Report header = new Report();
        header.setReportName("Report Type");
        header.setReportTimePeriod("Month/Year");
        header.setReportConsultant("Consultant");
        header.setReportItem("Schedule");
        reportList.add(header);
    
    //Run through appt list and match consultants and count        
        for(int i=0; i<reportConsultantList.size(); i++){  
        //Loop through ists
            for(int j=0; j<apptList.size();j++){
                Calendar appt = apptList.get(j);
                if(reportConsultantList.get(i).equals(apptList.get(j).getApptContact())){
                //Create new report object
                    Report report = new Report();
                     
                //Set initial report parameters
                    report.setReportName("Consultant Schedule");
                    report.setReportTimePeriod(reportMonth);
                    report.setReportConsultant(reportConsultantList.get(i));

                //Set Report item
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime start = LocalDateTime.parse(appt.getApptStartTime(), formatter);
                    LocalDateTime end = LocalDateTime.parse(appt.getApptEndTime(), formatter);
                    String item = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a").format(start) + " - " + DateTimeFormatter.ofPattern("HH:mm a").format(end) + " - " + appt.getApptTitle();
                    report.setReportItem(item);

                //Add Report item to report list
                    reportList.add(report);
                }
            }
        }
        System.out.println("Report List Size: " + reportList.size());
        System.out.println("First Item: " + reportList.get(0).getReportConsultant() + " - " + reportList.get(0).getReportItem());
    }
    //Appt count by Day for specified month
    public void runDayCount(){
        System.out.println("Monthly Count of appointments by day run");
        
        //Set Appt Headers as first item in reportList
        Report header = new Report();
        header.setReportName("Report Type");
        header.setReportTimePeriod("Month/Year");
        header.setReportConsultant("Day of Week");
        header.setReportItem("Count");
        reportList.add(header);
        
    //Run through appt list and match days of week and count        
        for(int i=0; i<reportDayList.size(); i++){
        //Create new report object
            Report report = new Report();
            
        //Set initial report parameters
            report.setReportName("Appointments By Day");
            report.setReportTimePeriod(reportMonth);
            report.setReportDay(reportDayList.get(i));
        
        //Initiate Count
            int count = 0;
            
        //Loop through ists
            for(int j=0; j<apptList.size();j++){
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String str = apptList.get(j).getApptStartTime();
                LocalDateTime formattedDate = LocalDateTime.parse(str,formatter);
                String day = DateTimeFormatter.ofPattern("EEEE").format(formattedDate);
                if(day.equals(reportDayList.get(i))){
                    count++;
                }
            }
            
        //Set Report count
            report.setReportItem(count + "");
            
        //Add Report item to report list
            reportList.add(report);
        }
        System.out.println("Report List Size: " + reportList.size());
        System.out.println("First Report Item: " + reportList.get(0).getReportDay() + " - " + reportList.get(0).getReportItem());
    }
    
//Add and populate table columns
    private void loadTable(){
        for (int i = 0; i < reportList.size(); i++) {
            ReportTableView.getItems().add(reportList);
        }
    }
    
//Load Appt Types
    public List<String> getApptType(){
        for(int i=0; i<apptList.size(); i++){
            String type = apptList.get(i).getApptDesc();
            if(reportTypeList.contains(type)){
            }
            else{
                reportTypeList.add(type);
            }
        }
        return reportTypeList;
    }
    public void loadDays(){
        reportDayList.add("Monday");
        reportDayList.add("Tuesday");
        reportDayList.add("Wednesday");
        reportDayList.add("Thursday");
        reportDayList.add("Firday");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Load report parameter lists
        try {
            //Load Consultants
            reportConsultantList = loadConsultList();
        } catch (SQLException ex) {
            Logger.getLogger(ReportViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Load Type List
        getApptType();
        loadDays();
        
    //Load Report Types
        reportTypeList.add("Appointment Type");
        reportTypeList.add("Consultant Schedule");
        reportTypeList.add("Appointments By Day");
        
    //Report Type Combo Options
        ReportTypeCombo.getItems().addAll(reportTypeList);
        
    //Month/Year Combo Options
        ReportMonthCombo.getItems().addAll("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12");
        ReportYearCombo.getItems().addAll(2015, 2016, 2017, 2018, 2019, 2020);
    }
}
