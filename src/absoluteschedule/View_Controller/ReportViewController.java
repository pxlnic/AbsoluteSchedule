/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import static absoluteschedule.AbsoluteSchedule.createConfirmAlert;
import static absoluteschedule.AbsoluteSchedule.createStandardAlert;
import absoluteschedule.Helper.ListManage;
import static absoluteschedule.Helper.ListManage.checkReminder;
import static absoluteschedule.Helper.ListManage.loadConsultList;
import static absoluteschedule.Helper.ListManage.loadMonthList;
import static absoluteschedule.Helper.ListManage.loadYearList;
import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    @FXML private ComboBox<String> ReportYearCombo;
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
    private List<String> reportApptTypeList = new ArrayList<>();
    private List<String> reportConsultantList = new ArrayList<>();
    private List<String> reportDayList = new ArrayList<>();
    private static List<String> reportMonthList = new ArrayList<>();
    private static List<String> reportYearList = new ArrayList<>();
    private ObservableList<Report> reportList = FXCollections.observableArrayList();
    private String reportMonth = "";
    private String exceptionMessage = new String();
    private ResourceBundle localization = loadResourceBundle();

//FXML Button Handlers  
    //Cancel Button handler
    @FXML void ReportCancelClick(ActionEvent event) throws IOException {
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
    @FXML void ReportClearClick(ActionEvent event) {
        Optional<ButtonType> confirm = createConfirmAlert(localization.getString("clear_confirm"), "Clear Fields?", "Confirm!");
        
        if(confirm.get() == ButtonType.OK){
            clearFields();
        }
        else{
            System.out.println("You clicked cancel. Please continue.");
        }
    }
    //View/Generate Report Button handler
    @FXML void ReportViewClick(ActionEvent event) throws SQLException {
    //Reset Exception Message
        exceptionMessage = "";
        
        //System.out.println("List size: " + reportList.size());
        reportList.clear();
        ReportTableView.getColumns().clear();
        //System.out.println("List size: " + reportList.size());
        
    //Get Text field entries
        String type = ReportTypeCombo.getValue();
        String month = ReportMonthCombo.getValue();
        String year = ReportYearCombo.getValue();
        
        try{
        //Test Entry Fields
            exceptionMessage = Report.isEntryValidView(exceptionMessage, type, year, month);
        
            if(exceptionMessage.length()>0){
                createStandardAlert(exceptionMessage, "Not all fields complete!", "Empty Fields!");
            }
            else{
            //Set Header Labels
                reportMonth = year + "-" + month;
                ReportNameLabel.setText(type);
                ReportDateLabel.setText(reportMonth);

            //Load appointment data for selected month
                String dateText = year + "-" + month + "-01";
                LocalDate selectedMonth = LocalDate.parse(dateText);
                //System.out.println(selectedMonth);
                ListManage l = new ListManage();
                l.seperateAppts(selectedMonth);

                apptList = l.getMonthsAppts();

            //Determine which report to run
                switch(ReportTypeCombo.getValue()){
                    case "Appointment Type":
                        runApptTypeCount();
                        loadTable("Appt Type", "Appt Count", "reportType");
                        break;
                    case "Consultant Schedule":
                        runConsultantAppt();
                        loadTable("Consultant Schedule", "Consultant", "reportConsultant");
                        break;
                    case "Appointments By Day":
                        runDayCount();
                        loadTable("Appointments By Day", "Day of Week", "reportDay");
                        break;
                    default:
                        System.out.println("No Selection Made");
                }
            }
        }
        catch(NumberFormatException e){
            
        }
    }
//Save Button handler
    @FXML void ReportSaveClick(ActionEvent event) {
    //Export data to csv/text file

    //Reset Exception message
        exceptionMessage = "";
    
    //Get Text field entries
        String type = ReportTypeCombo.getValue();
        String month = ReportMonthCombo.getValue();
        String year = ReportYearCombo.getValue();
        String title = ReportExportTitleField.getText().trim();
        String notes = ReportExportNotesField.getText().trim();
        
        try{
        //Test Entry Fields
            exceptionMessage = Report.isEntryValidSave(exceptionMessage, type, year, month, title, notes);
            
            if(exceptionMessage.length()>0){
                createStandardAlert(exceptionMessage, "Not all fields complete!", "Empty Fields!");
            }
            else{
                
            }
        }
        catch(NumberFormatException e){
            
        }
    }
    
//Report Generation Methods
    //Type of Appt Count Method
    public void runApptTypeCount() throws SQLException{
        System.out.println("Monthly Count of appointments by type run");
        
        getApptType();
        
    //Set Appt Headers as first item in reportList
        /*Report header = new Report();
        header.setReportName("Report Type");
        header.setReportTimePeriod("Month/Year");
        header.setReportType("Appt Type");
        header.setReportItem("Appt Count");
        reportList.add(header);*/
        
    //Run through appt type list and match types and count        
        for(int i=0; i<reportApptTypeList.size(); i++){
        //Create new report object
            Report report = new Report();
            
        //Set initial report parameters
            report.setReportName("Appointment Type");
            report.setReportTimePeriod(reportMonth);
            report.setReportType(reportApptTypeList.get(i));
            
            int count = 0;
            for(int j=0; j<apptList.size();j++){
                if(apptList.get(j).getApptDesc().equals(reportApptTypeList.get(i))){
                    count++;
                }
            }
        //Set Report count
            report.setReportItem(count + "");
            
        //Add Report item to report list
            reportList.add(report);
            //System.out.println("Count: " + count);
        }
        System.out.println("Report List Size: " + reportList.size());
        //System.out.println("Appt Count: " + reportList.get(0).getReportItem());

    }
    //Appointments Counts by Consultant  for specified month
    public void runConsultantAppt(){
        System.out.println("Monthly Count of appointments by consultnat run");
        
    //Set Appt Headers as first item in reportList
        /*Report header = new Report();
        header.setReportName("Report Type");
        header.setReportTimePeriod("Month/Year");
        header.setReportConsultant("Consultant");
        header.setReportItem("Schedule");
        reportList.add(header);*/
    
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
        //System.out.println("First Item: " + reportList.get(0).getReportConsultant() + " - " + reportList.get(0).getReportItem());
    }
    //Appt count by Day for specified month
    public void runDayCount(){
        System.out.println("Monthly Count of appointments by day run");
        
        //Set Appt Headers as first item in reportList
        /*Report header = new Report();
        header.setReportName("Report Type");
        header.setReportTimePeriod("Month/Year");
        header.setReportConsultant("Day of Week");
        header.setReportItem("Count");
        reportList.add(header);*/
        
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
        //System.out.println("First Report Item: " + reportList.get(0).getReportDay() + " - " + reportList.get(0).getReportItem());
    }
    
//Add and populate table columns
    private void loadTable(String colParameter, String colItem, String parameter){
    //Create Columns
        TableColumn nameCol = new TableColumn("Report Type");
        TableColumn periodCol = new TableColumn("Month/Year");
        TableColumn parameterCol = new TableColumn(colParameter);
        TableColumn itemCol = new TableColumn(colItem);
        
    //Attach Class parameters to Columns
        nameCol.setCellValueFactory(new PropertyValueFactory<Report, String>("reportName"));
        periodCol.setCellValueFactory(new PropertyValueFactory<Report, String>("reportTimePeriod"));
        parameterCol.setCellValueFactory(new PropertyValueFactory<Report, String>(parameter));
        itemCol.setCellValueFactory(new PropertyValueFactory<Report, String>("reportItem"));
        
    //Load Data and Columns
        ReportTableView.setItems(reportList);
        ReportTableView.getColumns().addAll(nameCol, periodCol, parameterCol, itemCol);
    }
    
//Load Appt Types & Days
    public List<String> getApptType(){
        reportTypeList.clear();
        
        for(int i=0; i<apptList.size(); i++){
            String type = apptList.get(i).getApptDesc();
            if(reportApptTypeList.contains(type)){
            }
            else{
                reportApptTypeList.add(type);
            }
        }
        return reportApptTypeList;
    }
    public void loadDays(){
        reportDayList.add("Monday");
        reportDayList.add("Tuesday");
        reportDayList.add("Wednesday");
        reportDayList.add("Thursday");
        reportDayList.add("Firday");
    }
    
//Clear Fields
    public void clearFields(){
        ReportTypeCombo.getSelectionModel().clearSelection();
        ReportMonthCombo.getSelectionModel().clearSelection();
        ReportYearCombo.getSelectionModel().clearSelection();
        ReportExportTitleField.setText("");
        ReportExportNotesField.setText("");
        ReportNameLabel.setText("Report Name");
        ReportDateLabel.setText("Month - Year");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    //Clear Lists
        reportConsultantList.clear();
        reportMonthList.clear();
        reportYearList.clear();
        reportDayList.clear();
        reportApptTypeList.clear();
        
    //Load report parameter lists
        try {
            //Load Consultants
            reportConsultantList = loadConsultList();
            //Load Month List
            reportMonthList = loadMonthList();
            //Load Year List
            reportYearList = loadYearList();
            System.out.println("Consultant: " + reportConsultantList.size() + ", Month: " + reportMonthList.size() + ", Year: " + reportYearList.size());
        } catch (SQLException ex) {
            Logger.getLogger(ReportViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Load Day List
        loadDays();

        
    //Load Report Types
        reportTypeList.add("Appointment Type");
        reportTypeList.add("Consultant Schedule");
        reportTypeList.add("Appointments By Day");
        
    //Report Type Combo Options
        ReportTypeCombo.getItems().addAll(reportTypeList);
        
    //Month/Year Combo Options
        ReportMonthCombo.getItems().addAll(reportMonthList);
        ReportYearCombo.getItems().addAll(reportYearList);
    }
}
