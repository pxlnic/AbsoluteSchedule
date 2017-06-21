/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import static absoluteschedule.AbsoluteSchedule.getCustList;
import static absoluteschedule.Helper.SQLManage.getConn;
import absoluteschedule.Model.Calendar;
import absoluteschedule.Model.Customer;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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
    @FXML private ComboBox<Integer> CalendarEndTimeHourCombo;
    @FXML private ComboBox<Integer> CalendarEndTimeMinCombo;
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
    
    //Month Tab
    @FXML private Button CalendarMonthBackButton;
    @FXML private Label CalendarMonthTabHeader;
    @FXML private Button CalendarMonthNextButton;
    @FXML private GridPane CalendarMonthGrid;
    
//Instance Variables
    private List<Customer> calCustList = new ArrayList<>();
    private List<String> custNames = new ArrayList<>();
    private List<Integer> hourList = new ArrayList<>();
    private List<Integer> minList = new ArrayList<>();
    private List<String> locList = new ArrayList<>();
    private List<String> consultantList = new ArrayList<>();
    
//SQL DB Variables
    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private ResourceBundle localization;
    
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
        CalendarEndTimeHourCombo.getItems().clear();
        CalendarEndTimeMinCombo.getItems().clear();
        CalendarCustomerCombo.getItems().clear();
        CalendarLocationCombo.getItems().clear();
        CalendaryAllDayCheckbox.setSelected(false);
        CalendarTitleField.setText("");
        CalendarDescriptionArea.setText("");
    }
    //Save Button handler
    @FXML void CalendarSaveClick(ActionEvent event) {
    //Variables
        int apptID = -1;
        int custID = getCustID(CalendarCustomerCombo.getValue());
        Calendar tempAppt = new Calendar();
        
    //Getting textfield entries for appt
        tempAppt.setCustID(custID);
        tempAppt.setApptTitle(CalendarTitleField.getText().trim());
        tempAppt.setApptDesc(CalendarDescriptionArea.getText().trim());
        tempAppt.setApptDate(CalendarDatePicker.getValue() + " ");
        tempAppt.setApptStartTime(CalendarTimeHourCombo.getValue(), CalendarTimeMinCombo.getValue());
        tempAppt.setApptEndTime(CalendarEndTimeHourCombo.getValue(), CalendarEndTimeMinCombo.getValue());
        tempAppt.setApptLoc(CalendarLocationCombo.getValue());
        tempAppt.setApptContact(CalendarConsultantCombo.getValue());
        
    //Validate if appointment is already in system
        
    //Add appointment
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
        calCustList = getCustList();
        loadCustNames();
        try {
            loadLocList();
            loadConsultList();
        } catch (SQLException ex) {
            Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadHour();
        loadMin();

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
    
//Load hours and min into ArrayList
    private void loadHour(){
        for(int i=0; i<24; i++){
            hourList.add(i);
            System.out.println("Hour: " + i + " was added to list.");
        }
    }
    private void loadMin(){
        for(int i=1; i<12; i++){
            minList.add(i*5);
            System.out.println("Min : " + i*5 + " was added to list.");
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
        try{
            conn = getConn();
            ps = conn.prepareStatement("SELECT * FROM city");
            rs = ps.executeQuery();
            
            while(rs.next()){
                String loc = rs.getString("city");
                locList.add(loc);
                System.out.println("Location: " + loc + " was added to list.");
            }
        }
        catch(SQLException err){
            
        }
        finally{
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } 
    }
    
//Load consultants to arraylist
    private void loadConsultList() throws SQLException{
        try{
            conn = getConn();
            ps = conn.prepareStatement("SELECT * FROM user");
            rs = ps.executeQuery();
            
            while(rs.next()){
                String user = rs.getString("userName");
                consultantList.add(user);
                System.out.println("Consultant: " + user + " was added to list.");
            }
        }
        catch(SQLException err){
            
        }
        finally{
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } 
    }
}

