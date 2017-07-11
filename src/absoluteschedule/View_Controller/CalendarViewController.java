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
import static absoluteschedule.Helper.ListManage.getMonthsAppts;
import static absoluteschedule.Helper.ListManage.getWeeksAppts;
import static absoluteschedule.Helper.ListManage.loadAppts;
import static absoluteschedule.Helper.ListManage.loadConsultList;
import static absoluteschedule.Helper.ListManage.loadCustomers;
import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
import static absoluteschedule.Helper.SQLManage.getConn;
import absoluteschedule.Model.Calendar;
import static absoluteschedule.Model.Calendar.convertToUTC;
import static absoluteschedule.Model.Calendar.isEntryValid;
import absoluteschedule.Model.Customer;
import static absoluteschedule.View_Controller.LogInController.loggedOnUser;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
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
    @FXML private TextField CalendarIDField;
    @FXML private DatePicker CalendarDatePicker;
    @FXML private ComboBox<String> CalendarTimeHourCombo;
    @FXML private ComboBox<String> CalendarTimeMinCombo;
    @FXML private ComboBox<String> CalendarEndTimeHourCombo;
    @FXML private ComboBox<String> CalendarEndTimeMinCombo;
    @FXML private CheckBox CalendaryAllDayCheckbox;
    @FXML private ComboBox<String> CalendarCustomerCombo;
    @FXML private ComboBox<String> CalendarLocationCombo;
    @FXML private ComboBox<String> CalendarConsultantCombo;
    @FXML private TextField CalendarTitleField;
    @FXML private TextArea CalendarDescriptionArea;
    @FXML private Button CalendarCancelButton;
    @FXML private Button CalendarClearButton;
    @FXML private Button CalendarSaveButton;
    
    //Week Tab
    @FXML private GridPane CalendarWeekGrid;
    @FXML private VBox CalendarWeekSunday;
    @FXML private VBox CalendarWeekMonday;
    @FXML private VBox CalendarWeekTuesday;
    @FXML private VBox CalendarWeekWednesday;
    @FXML private VBox CalendarWeekThursday;
    @FXML private VBox CalendarWeekFriday;
    @FXML private VBox CalendarWeekSaturday;
    
    //Month Tab
    @FXML private GridPane CalendarMonthGrid;
    @FXML private Button CalendarMonthBackButton;
    @FXML private Label CalendarMonthTabHeader;
    @FXML private Button CalendarMonthNextButton;
    
//Instance Variables
    private List<Customer> calCustList = new ArrayList<>();
    private List<String> custNames = new ArrayList<>();
    private List<String> hourList = new ArrayList<>();
    private List<String> minList = new ArrayList<>();
    private List<String> locList = new ArrayList<>();
    private List<String> consultantList = new ArrayList<>();
    private List<Calendar> thisWeeksAppts = new ArrayList<>();
    private List<Calendar> thisMonthsAppts = new ArrayList<>();
    private String user = loggedOnUser();
    private int apptID = -1;
    private String exceptionMessage = "";
    private LocalDate selectedMonth = LocalDate.now();
    
//SQL DB Variables
    private ResultSet rs = null;
    private ResourceBundle localization = loadResourceBundle();
    
//Footer Button handlers
    //Cancel Button handler
    @FXML void CalendarCancelClick(ActionEvent event) throws IOException {
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
    @FXML void CalendarClearClick(ActionEvent event) {
        Optional<ButtonType> confirm = createConfirmAlert(localization.getString("clear_confirm"), "Clear Fields?", "Confirm!");
        
        if(confirm.get() == ButtonType.OK){
            clearFields();
        }
        else{
            System.out.println("You clicked cancel. Please continue.");
        }
    }
    //Save Button handler
    @FXML void CalendarSaveClick(ActionEvent event) throws SQLException {
    //Clear Exception Message
        exceptionMessage = "";
        
    //Variables
        if(CalendarIDField.getText().trim().isEmpty()){
            apptID = -1;
        }
        else{
            apptID = Integer.parseInt(CalendarIDField.getText().trim());
        }
        int custID = -1;
        //Entry field data
        String date = CalendarDatePicker.getValue() + " ";
        String startHour = CalendarTimeHourCombo.getValue();
        String startMin = CalendarTimeMinCombo.getValue();
        String endHour = CalendarEndTimeHourCombo.getValue();
        String endMin = CalendarEndTimeHourCombo.getValue();
        boolean allDay = CalendaryAllDayCheckbox.isSelected();
        String customerName = CalendarCustomerCombo.getValue();
        String consultantName = CalendarConsultantCombo.getValue();
        String location = CalendarLocationCombo.getValue();
        String title = CalendarTitleField.getText().trim();
        String desc = CalendarDescriptionArea.getText().trim();

        
        try{
        //Test Entry Fields
            exceptionMessage = isEntryValid(exceptionMessage, date, startHour, startMin, endHour, endMin, allDay, customerName, consultantName, location, title, desc);
        
            if(exceptionMessage.length()>0){
                createStandardAlert(exceptionMessage, "Not all fields complete!", "Empty Fields!");
            }
            else{
            //Temp appointment/customer constructors
                Calendar tempAppt = new Calendar();
                Customer tempCust = new Customer();
                List<Calendar> tempApptList = new ArrayList<>();


            //Testing setting date/time
                String startTime = date + startHour + ":" + startMin;
                String endTime = date + endHour + ":" + endMin;


            //Get UTC and Local Times
                String startUtc = convertToUTC(startTime);
                String endUtc = convertToUTC(endTime);

                System.out.println("UTC Start Time: " + startUtc);
                System.out.println("UTC End Time: " + endUtc);

            //Get Customer ID
                tempCust.setCustName(customerName);
                custID = getCustID(customerName);

            //Getting textfield entries for appt
                tempAppt.setCustID(custID);
                tempAppt.setApptTitle(title);
                tempAppt.setApptDesc(desc);
                tempAppt.setApptStartTime(startUtc);
                tempAppt.setApptEndTime(endUtc);
                tempAppt.setApptLoc(location);
                tempAppt.setApptContact(consultantName);

            //Validate if appointment is already in system
                tempApptList = tempAppt.isApptValid(tempAppt.getCustID(), tempAppt.getApptStartTime(), tempAppt.getApptEndTime(), tempAppt.getApptContact());

            //Add or update appointment
                if(tempApptList.size()>0 || apptID>-1){
                    if(CalendarIDField.getText().trim().isEmpty()){
                        //Error Message
                        exceptionMessage = exceptionMessage + "-This appointment conflicts with " + tempApptList.size() + " appointments." ;
                    }
                    else{
                        //Update appointment
                        System.out.println("Appointment updated");
                        tempAppt.updateAppt(Integer.parseInt(CalendarIDField.getText().trim()),custID, tempAppt.getApptTitle(), tempAppt.getApptDesc(), tempAppt.getApptLoc(), tempAppt.getApptContact(), "url", startUtc, endUtc, user); 
                    }
                }
                else{
                    System.out.println("Appointment added.");
                    tempAppt.addAppt(custID, tempAppt.getApptTitle(), tempAppt.getApptDesc(), tempAppt.getApptLoc(), tempAppt.getApptContact(), "url", startUtc, endUtc, user);
                }

            //Reload Appts
                reloadCalAppts();
                
            //Clear fields
                clearFields();
            }
        }
        catch(NumberFormatException e){
            
        }
    }
    //Month Back Button handler
    @FXML void CalendarMonthBackClick(ActionEvent event) throws SQLException {
        selectedMonth = selectedMonth.minusMonths(1);
        reloadCalAppts();
    }
    //Month Next Button handler
    @FXML void CalendarMonthNextClick(ActionEvent event) throws SQLException {
        selectedMonth = selectedMonth.plusMonths(1);
        reloadCalAppts();
    }
    
//Get customerID
    private int getCustID(String name){
        int id = -1;
        for(int i=0; i<calCustList.size(); i++){
            if(calCustList.get(i).getCustName().equals(name)){
                id = calCustList.get(i).getCustID();
            }
        }
        return id;
    }
    
//Load ComboBox Results
    //Load hours and min into ArrayList
    private void loadHour(){
        for(int i=6; i<21; i++){
            if(i<10){
                hourList.add("0"+i);
            }
            else{
            hourList.add(""+i);
            }
        }
    }
    private void loadMin(){
        for(int i=0; i<12; i++){
            if(i<2){
                minList.add("0"+(i*5));
            }
            else{
                minList.add(""+(i*5));
            }
        }
    }
    //Load customer names to arraylist
    private void loadCustNames(){
        String name = "";
        for(int i=0; i<calCustList.size(); i++){
            name = calCustList.get(i).getCustName();
            custNames.add(name);
        }
    }
    //Load locations to arraylist
    private void loadLocList() throws SQLException{
        try(Connection conn = getConn();
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM city")){;
            rs = ps.executeQuery();
            
            while(rs.next()){
                String loc = rs.getString("city");
                locList.add(loc);
            }
        }
        catch(SQLException err){
            err.printStackTrace();
        } 
    }
    
//Load Appts to GUI
    //Load Weekly Appts
    public void loadWeeksAppts(){
        LocalDate today = LocalDate.now();
    
        for(int i=0; i<7; i++){
        //Set current day
            LocalDate dayOfWeek = today.with(DayOfWeek.MONDAY).plusDays(i-1);
            
        //Create nodes
            VBox day = new VBox();                                                          //Main container for the day
            HBox date = new HBox();                                                         //Container for Day of Month Label
            Label dateNum = new Label(DateTimeFormatter.ofPattern("d").format(dayOfWeek));  //Label for Day Month
            VBox apptList = new VBox();                                                     //Container for all appts for that day
            
        //Add Appts
            for(int a=0; a<thisWeeksAppts.size(); a++){
            //VBox for appt
                VBox appt = new VBox();
                Calendar thisAppt = thisWeeksAppts.get(a);
            
            //Convert LocalDateTime to LocalDate
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime time = LocalDateTime.parse(thisWeeksAppts.get(a).getApptStartTime(), formatter);
                LocalDate apptDate = time.toLocalDate();
                
            //If appt date is same as current day then appt card is created
                if(apptDate.isEqual(dayOfWeek)){
                //Time of meeting
                    String localTimeStr = DateTimeFormatter.ofPattern("hh:mm a").format(time);
                    Label tempTime = new Label(localTimeStr);
                //Title of meeting
                    Label tempTitle = new Label(thisWeeksAppts.get(a).getApptTitle());
                //Consultant and customer meeting
                    Label tempPersons = new Label("Consultant: " + thisWeeksAppts.get(a).getApptContact());
                    
                //Format Nodes
                    tempTime.setFont(new Font(12));
                    tempTitle.setFont(new Font(10));
                    tempPersons.setFont(new Font(10));
                    day.setMargin(appt, new Insets(5,2.5,0,2.5));
                    appt.setPadding(new Insets(5,5,5,5));
                    appt.setMargin(tempTime, new Insets(0,0,5,0));
                    appt.setMargin(tempTitle, new Insets(0,0,5,5));
                    appt.setMargin(tempPersons, new Insets(0,0,5,5));
                    appt.setStyle("-fx-border-style: solid;" +
                                  "-fx-border-color: C8C8C8;" +
                                  "-fx-background-color: E6E6E6");

                //Add appts to VBox
                    appt.getChildren().addAll(tempTime, tempTitle, tempPersons);
                    apptList.getChildren().add(appt);
                }
                
            //Add MouseEvent to Appt VBox
                appt.setOnMouseClicked(new EventHandler<MouseEvent>(){
                    @Override
                    public void handle(MouseEvent event) {
                        System.out.println("Appt: ID: " + thisAppt.getApptID() + " was selected.");
                        //Retrieve Date, Start Time, End Time
                        
                        Customer thisCust = new Customer();

                        for(int y=0; y<calCustList.size(); y++){
                            if(thisAppt.getCustID()==calCustList.get(y).getCustID()){
                                thisCust = calCustList.get(y);
                            }
                        }
                        
                        CalendarIDField.setText(Integer.toString(thisAppt.getApptID()));
                        CalendarTitleField.setText(thisAppt.getApptTitle());
                        CalendarDescriptionArea.setText(thisAppt.getApptDesc());
                        CalendarCustomerCombo.setValue(thisCust.getCustName());
                        CalendarConsultantCombo.setValue(thisAppt.getApptContact());
                        CalendarLocationCombo.setValue(thisAppt.getApptLoc());
                    }
                });
            }
            
        //Format nodes
            date.setPadding(new Insets(5,5,5,5));
            date.setAlignment(Pos.BASELINE_RIGHT);
            if(i==0 || i==6){
                day.setStyle("-fx-background-color: E6E6E6");
            }
            
        //Add nodes
            date.getChildren().add(dateNum);
            day.getChildren().addAll(date, apptList);
            CalendarWeekGrid.add(day, i, 0);
        }
    }
    //Load Monthly Appts
    //***Only the first 4 appts show for the day on month calendar tab***
    public void loadMonthsAppts(LocalDate thisDate){
        LocalDate today = thisDate;
        LocalDate firstOfMonth = today.with(TemporalAdjusters.firstDayOfMonth());
        int firstDayIndex = Integer.parseInt(DateTimeFormatter.ofPattern("e").format(firstOfMonth))-1;
        CalendarMonthTabHeader.setText(DateTimeFormatter.ofPattern("MMMM").format(firstOfMonth));
        
        //System.out.println(DateTimeFormatter.ofPattern("eee").format(firstOfMonth));
        //System.out.println(firstDayIndex);
        
        int count = 0;
        
    //For loop for row (Week)
        for(int i=0; i<6; i++){
        //For loop for column (Day)
            for(int j=0; j<7; j++){
                int dailyApptCount = 1;
                
            //Set day of month
                LocalDate dayOfMonth = firstOfMonth.plusDays(count-firstDayIndex);
                int dayNumOfMonth = Integer.parseInt(DateTimeFormatter.ofPattern("d").format(dayOfMonth));
                
            //Create nodes
                VBox day = new VBox();                      //Main container for day
                HBox date = new HBox();                     //Container for Day of Month Label
                Label text = new Label(dayNumOfMonth + ""); //Label for Day Month
                VBox apptList = new VBox();                 //Container for all appts for that day
                
            //Create Appt cards to add to appt VBox
                for(int a=0; a<thisMonthsAppts.size(); a++){
                    
                //VBox for appt
                    VBox appt = new VBox();
                //Convert LocalDateTime to LocalDate
                    Calendar thisAppt = thisMonthsAppts.get(a);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime time = LocalDateTime.parse(thisAppt.getApptStartTime(), formatter);
                    LocalDate apptDate = time.toLocalDate();

                //If appt date is same as current day then appt card is created
                    if(apptDate.isEqual(dayOfMonth) && dailyApptCount<5){
                    //System.out.println("Date: " + apptDate + " was added successfully.");
                        
                    //Time of meeting
                        String localTimeStr = DateTimeFormatter.ofPattern("hh:mm a").format(time) + " - " + thisMonthsAppts.get(a).getApptContact();
                        Label apptLabel = new Label(localTimeStr);

                    //Format nodes
                        apptLabel.setFont(new Font(8));
                        day.setMargin(appt, new Insets(2.5,2.5,0,2.5));
                        appt.setPadding(new Insets(1,1,1,1));
                        appt.setMargin(apptLabel, new Insets(0,0,0,0));
                        appt.setStyle("-fx-border-style: solid;" +
                                      "-fx-border-color: C8C8C8;" +
                                      "-fx-background-color: E6E6E6");

                    //Add appts to VBox
                        appt.getChildren().addAll(apptLabel);
                        apptList.getChildren().add(appt);
                        
                        //System.out.println(dailyApptCount);
                        dailyApptCount++;
                    }
                    
                //Add MouseEvent to Appt VBox
                    appt.setOnMouseClicked(new EventHandler<MouseEvent>(){
                        @Override
                        public void handle(MouseEvent event) {
                            System.out.println("Appt: ID: " + thisAppt.getApptID() + " was selected.");
                            //Retrieve Date, Start Time, End Time

                            Customer thisCust = new Customer();

                            for(int y=0; y<calCustList.size(); y++){
                                if(thisAppt.getCustID()==calCustList.get(y).getCustID()){
                                    thisCust = calCustList.get(y);
                                }
                            }

                            CalendarIDField.setText(Integer.toString(thisAppt.getApptID()));
                            CalendarTitleField.setText(thisAppt.getApptTitle());
                            CalendarDescriptionArea.setText(thisAppt.getApptDesc());
                            CalendarCustomerCombo.setValue(thisCust.getCustName());
                            CalendarConsultantCombo.setValue(thisAppt.getApptContact());
                            CalendarLocationCombo.setValue(thisAppt.getApptLoc());
                        }
                    });
                    
                }
                
            //Add nodes
                date.getChildren().add(text);
                day.getChildren().addAll(date, apptList);
                CalendarMonthGrid.add(day, j, i);
                count++;
                
            //Format nodes
                date.setPadding(new Insets(0,0,0,0));
                date.setAlignment(Pos.BASELINE_RIGHT);
                if(j==0 || j==6 || (i==0 && j<firstDayIndex) || (i>3 && dayNumOfMonth<14)){
                    day.setStyle("-fx-background-color: E6E6E6");
                }
            }
        }
        
    } 
    
//Load/Reload All Appts
    public void reloadCalAppts() throws SQLException{    
    //Clear GridPane children so can be reloaded
        CalendarMonthGrid.getChildren().clear();
        CalendarWeekGrid.getChildren().clear();
        
    //Clear Appt ArrayLists
        thisWeeksAppts.clear();
        thisMonthsAppts.clear();
        
    //Load Weekly and Monhthly Data
        ListManage l = new ListManage();
        l.seperateAppts(selectedMonth);
        
        thisWeeksAppts = getWeeksAppts();
        thisMonthsAppts = getMonthsAppts();
        System.out.println("Week: " + thisWeeksAppts.size() + ", Month: " + thisMonthsAppts.size());
        
        loadWeeksAppts();
        loadMonthsAppts(selectedMonth);
    }
    
//Clear Fields
    public void clearFields(){
        CalendarIDField.setText("");
        CalendarDatePicker.setValue(LocalDate.now());
        CalendarTimeHourCombo.getSelectionModel().clearSelection();
        CalendarTimeMinCombo.getSelectionModel().clearSelection();
        CalendarEndTimeHourCombo.getSelectionModel().clearSelection();
        CalendarEndTimeMinCombo.getSelectionModel().clearSelection();
        CalendarCustomerCombo.getSelectionModel().clearSelection();
        CalendarConsultantCombo.getSelectionModel().clearSelection();
        CalendarLocationCombo.getSelectionModel().clearSelection();
        CalendaryAllDayCheckbox.setSelected(false);
        CalendarTitleField.setText("");
        CalendarDescriptionArea.setText("");
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedMonth = LocalDate.now();
        
    //Reset Error Message
        exceptionMessage = "";
        
        loadCustNames();
        try {
            loadAppts();
            loadLocList();
            consultantList = loadConsultList();
            calCustList = loadCustomers();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadHour();
        loadMin();
        
    //Load appointments
        try {
            //Load Weekly and Monhthly Data
            reloadCalAppts();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    //Populate Datepicker
        CalendarDatePicker.setValue(LocalDate.now());
        
    //Populate Hour/Min Combo Boxes
        CalendarTimeHourCombo.getItems().addAll(hourList);
        CalendarTimeMinCombo.getItems().addAll(minList);
        
    //Populate Duration Combo Box
        CalendarEndTimeHourCombo.getItems().addAll(hourList);
        CalendarEndTimeMinCombo.getItems().addAll(minList);
        
    //Populate Customer Combo Box
        CalendarCustomerCombo.getItems().addAll(custNames);
        
    //Populate Consultant Combo Box
        CalendarConsultantCombo.getItems().addAll(consultantList);
        
    //Populate Location Combo Box
        CalendarLocationCombo.getItems().addAll(locList);
    }
    
}

