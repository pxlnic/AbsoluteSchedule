/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import absoluteschedule.AbsoluteSchedule;
import static absoluteschedule.AbsoluteSchedule.getApptList;
import static absoluteschedule.AbsoluteSchedule.getMainCustList;
import static absoluteschedule.AbsoluteSchedule.reloadMainApptList;
import absoluteschedule.Model.Calendar;
import static absoluteschedule.Model.Calendar.convertToLocal;
import absoluteschedule.Model.Customer;
import static absoluteschedule.View_Controller.LogInController.loggedOnUser;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author NicR
 */
public class MainViewController implements Initializable {

//FXML Declarations
//Agenda Container
    @FXML private VBox MainAgendaVbox;
    
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
    private List<Calendar> todaysAppts = new ArrayList<>();
    private static List<Calendar> weeksAppts = new ArrayList<>();
    private static List<Calendar> monthsAppts = new ArrayList<>();
    private List<Customer> localMainCustList = new ArrayList<>();
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
        
    //Load appointment data
        try {
            // TODO
            reloadMainApptList();
        } catch (SQLException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        apptList = getApptList();
        getApptCount();
        System.out.println("Count of appts: " + apptList.size());
        
    //Set Welcome message and appointments counts
        MainWelcomeLabel.setText("Welcome, " + loggedOnUser());
        MainTodaysApptCountLabel.setText("Daily Appt. Count: " + todaysCount);
        MainWeekApptCountLabel.setText("Weekly Appt. Count: " + thisWeeksCount);
        MainMonthApptCountLabel.setText("Monthly Appt. Count: " + thisMonthsCount);
        
    //Update Time
        updateClock();
        
    //Load Agenda Items
        for(int i=0; i<todaysAppts.size(); i++){
            newAgendaItem(i);
        }
    }    
    
//Set mainApp to the main application.
    public void setMainApp(AbsoluteSchedule mainApp) {
        this.mainApp = mainApp;
    }
    
//Get appointment counts
    public void getApptCount(){
    //Clear weekly and monthly arraylists to be reset
        weeksAppts.clear();
        monthsAppts.clear();
        
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
                todaysAppts.add(apptList.get(i));
            }
            else{
                System.out.println("Date: " + date + " is not today");
            }
            if(date.isAfter(todayFormatted.with(DayOfWeek.MONDAY).minusDays(1)) && date.isBefore(todayFormatted.with(DayOfWeek.FRIDAY).plusDays(1))){
                System.out.println("Date: " + date + " is within current week.");
                thisWeeksCount = thisWeeksCount+1;
                weeksAppts.add(apptList.get(i));
            }
            else{
                System.out.println("Date: " + date + " is not within current week.");
            }
            if(date.isAfter(todayFormatted.with(TemporalAdjusters.firstDayOfMonth()).minusDays(1)) && date.isBefore(todayFormatted.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1))){
                System.out.println("Date: " + date + " is within current month.");
                thisMonthsCount = thisMonthsCount+1;
                monthsAppts.add(apptList.get(i));
            }
            else{
                System.out.println("Date: " + date + " is not within this month.");
            }
        }
        System.out.println("Today: " + todaysCount + ", This Week: " + thisWeeksCount + ", This Month: " + thisMonthsCount);
    }
    
//Get customer names
    public void getCustNames(){
        localMainCustList = getMainCustList();
    }
    
//Update Time
    public void updateClock(){
    //
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), ev -> {
            MainDateTimeLabel.setText(DateTimeFormatter.ofPattern("MMM, dd yyyy  h:mm a").format(LocalDateTime.now()));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play(); 

    }
    
//Create new HBox for agenda and format
    public void newAgendaItem(int j){
    //ArrayList for customers
        getCustNames();
        
    //Create & add new HBox
        VBox temp = new VBox();
        MainAgendaVbox.getChildren().add(temp);
        
    //Create labels
        //Time of meeting
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
        LocalDateTime time = LocalDateTime.parse(todaysAppts.get(j).getApptStartTime(), formatter);
        String localTimeStr = convertToLocal(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(time));     
        Label tempTime = new Label(localTimeStr);
        //Title of meeting
        Label tempTitle = new Label(todaysAppts.get(j).getApptTitle());
        //Consultant and customer meeting
        String custName = localMainCustList.get(todaysAppts.get(j).getCustID()-1).getCustName();
        Label tempPersons = new Label(todaysAppts.get(j).getApptContact() + " meeting with " + custName);
        
    //Format Labels
        MainAgendaVbox.setMargin(temp, new Insets(15,15,0,15));
        temp.setPadding(new Insets(10,10,10,10));
        temp.setMargin(tempTime, new Insets(0,0,5,5));
        temp.setMargin(tempTitle, new Insets(0,0,5,15));
        temp.setMargin(tempPersons, new Insets(0,0,5,15));
        temp.setStyle("-fx-border-style: solid;" +
                      "-fx-border-color: C8C8C8;" +
                      "-fx-background-color: E6E6E6");
    
    //Add Labels
        temp.getChildren().addAll(tempTime, tempTitle, tempPersons);

    }
    
//Return weekly and monthly appts
    public static List<Calendar> getWeeksAppts(){
        return weeksAppts;
    }
    public static List<Calendar> getMonthsAppts(){
        return monthsAppts;
    }
}
