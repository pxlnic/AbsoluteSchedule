/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import absoluteschedule.AbsoluteSchedule;
import static absoluteschedule.AbsoluteSchedule.getApptList;
import absoluteschedule.Model.Calendar;
import static absoluteschedule.View_Controller.LogInController.loggedOnUser;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javax.swing.text.DateFormatter;

/**
 * FXML Controller class
 *
 * @author NicR
 */
public class MainViewController implements Initializable {

//FXML Declarations
//Header Labels
    @FXML private Label MainWelcomeLabel;
    @FXML private Label MainDateTimeLabel;

//Appointment Count Labels
    @FXML private Label MainTodaysApptCountLabel;
    @FXML private Label MainWeekApptCountLabel;
    @FXML private Label MainMonthApptCountLabel;

//Buttons to different screens
    @FXML private Button MainCalendarButton;
    @FXML private Button MainCustomerButton;
    @FXML private Button MainReportButton;

//Footer Buttons to manage appointments in agenda and exit application
    @FXML private Button MainDeleteApptButton;
    @FXML private Button MainEditApptButton;
    @FXML private Button MainExitButton;
    
//Instance Variables
    private AbsoluteSchedule mainApp;
    private List<Calendar> apptList = new ArrayList<>();
    private int todaysCount = 0;
    private int thisWeeksCount = 0;
    private int thisMonthsCount = 0;
        
//Constructor
    public MainViewController(){
    }
    
//Button controls

//Main Calendar Button handler
    @FXML void MainCalendarClicked(ActionEvent event) throws IOException {
        System.out.println("Calendar clicked");
        
    //Load Add Parts Screen
        Parent calendarView = FXMLLoader.load(getClass().getResource("CalendarView.fxml"));
        Scene scene = new Scene(calendarView);
        
    //Loads stage information from main file
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
    //Load scene onto stage
        window.setScene(scene);
        window.show();
    }
//Main Customer Button handler
    @FXML void MainCustomerClicked(ActionEvent event) throws IOException {
        System.out.println("Customer clicked");
        
    //Load Add Parts Screen
        Parent customerView = FXMLLoader.load(getClass().getResource("CustomerView.fxml"));
        Scene scene = new Scene(customerView);
        
    //Loads stage information from main file
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
    //Load scene onto stage
        window.setScene(scene);
        window.show();
    }
//Main Report Button handler
    @FXML void MainReportClicked(ActionEvent event) throws IOException {
        System.out.println("Report clicked");
        
    //Load Add Parts Screen
        Parent reportView = FXMLLoader.load(getClass().getResource("ReportView.fxml"));
        Scene scene = new Scene(reportView);
        
    //Loads stage information from main file
        Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
        
    //Load scene onto stage
        window.setScene(scene);
        window.show();
    }
//Main Delete Button handler
    @FXML void MainDeleteClicked(ActionEvent event) {

    }
//Main Edit Button handler
    @FXML void MainEditClicked(ActionEvent event) {

    }
//Main Exit Button handler
    @FXML void MainExitClicked (ActionEvent event) {
        System.out.println("Exit clicked!");
        System.exit(0);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        apptList = getApptList();
        getApptCount();
        System.out.println("Count of appts: " + apptList.size());
        
        MainWelcomeLabel.setText("Welcome, " + loggedOnUser());
        MainTodaysApptCountLabel.setText("Daily Appt. Count: " + todaysCount);
        MainWeekApptCountLabel.setText("Weekly Appt. Count: " + thisWeeksCount);
        MainMonthApptCountLabel.setText("Monthly Appt. Count: " + thisMonthsCount);
    }    
    
//Set mainApp to the main application.
    public void setMainApp(AbsoluteSchedule mainApp) {
        this.mainApp = mainApp;
    }
    
//Get appointment counts
    public void getApptCount(){
        LocalDate today = LocalDate.now();
        for(int i = 0; i < apptList.size(); i++){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = apptList.get(i).getApptStartTime();
            String subDate = dateString.substring(0,10);
            String subToday = formatter.format(today);
            LocalDate date = LocalDate.parse(subDate, formatter);
            LocalDate todayFormatted = LocalDate.parse(subToday, formatter);
            if(date.equals(todayFormatted)){
                System.out.println("Date :" + date + " is today.");
                todaysCount = todaysCount+1;
            }
            else{
                System.out.println("Date: " + date + " is not today");
            }
            if(date.isAfter(todayFormatted.with(DayOfWeek.MONDAY)) && date.isBefore(todayFormatted.with(DayOfWeek.FRIDAY))){
                System.out.println("Date: " + date + " is within current week.");
                thisWeeksCount = thisWeeksCount+1;
            }
            else{
                System.out.println("Date: " + date + " is not within current week.");
            }
            if(date.isAfter(todayFormatted.with(TemporalAdjusters.firstDayOfMonth())) && date.isBefore(todayFormatted.with(TemporalAdjusters.lastDayOfMonth()))){
                System.out.println("Date: " + date + " is within current month.");
                thisMonthsCount = thisMonthsCount+1;
            }
            else{
                System.out.println("Date: " + date + " is not within this month.");
            }
        }
        System.out.println("Today: " + todaysCount + ", This Week: " + thisWeeksCount + ", This Month: " + thisMonthsCount);
    }
}
