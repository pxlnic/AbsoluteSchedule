/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.View_Controller;

import absoluteschedule.AbsoluteSchedule;
import static absoluteschedule.Helper.SQLManage.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author NicR
 */
public class LogInController implements Initializable {

//FXML Declarations
    @FXML private PasswordField LoginPasswordField;
    @FXML private TextField LoginUserNameField;
    @FXML private Button LoginSubmitButton;
    @FXML private Button LoginExitButton;
    @FXML private Label LoginMessageLabel;
    
//Instance Variables
    private AbsoluteSchedule mainApp;
    private Connection conn;
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    
//FXML Button Handlers

//Login Exit Button handler
    @FXML void LoginExitClicked(ActionEvent event) {
        System.out.println("Exit clicked!");
        System.exit(0);
    }
//Login Login Button Clicked
    @FXML void LoginSubmitClicked(ActionEvent event) throws IOException, SQLException, InterruptedException {
        System.out.println("Login clicked");
        
    //Capture textfield info
        String userName = LoginUserNameField.getText().trim();
        String password = LoginPasswordField.getText().trim();
        
    //Login Success/Failure Boolean & Message
        boolean isLoginValid = false;
        String logMessage = "";
        
    //Try statement to run query and complete login
        try{
        //open connection
            //String dbText = getConnText();
            Connection conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U04H1H", "U04H1H","53688238168");

        //Validating textfield data was gathered and SQL Query values to select instnatiated
            System.out.println("Username: " + userName);
            System.out.println("Password: " + password);
            String dbUserName = "";
            String dbPassword = "";

        //Query to check macthing
            ps = conn.prepareStatement("SELECT * FROM user WHERE userName=? AND password=?");
            ps.setString(1, userName);
            ps.setString(2, password);
            rs = ps.executeQuery();
            System.out.println("SQL Query successful");
            
            while(rs.next()){
                System.out.println("Setting variables from SQL Query");
                dbUserName = rs.getString("userName");
                dbPassword = rs.getString("password");
                System.out.println("Username: " + dbUserName + ", Password: " + dbPassword);
            }
            
            if(userName.equals(dbUserName) && password.equals(dbPassword)){
                LoginMessageLabel.setText("Login Successful!");
                
            //Log File Message and Login Boolean updated
                logMessage = "Successful login attempt by User: " + userName + " at " + "Time" + ".";
                System.out.println(logMessage);
                isLoginValid = true;
                
            //Wait 3 seconds
                TimeUnit.SECONDS.sleep(3);
                
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
                LoginMessageLabel.setText("Login Credentials Incorrect");
            
            //Log File Message and Login Boolean updated
                logMessage = "Failed login attempt by User: " + userName + " at " + "Time" + ".";
                System.out.println(logMessage);
                isLoginValid = false;
            }
        }
        catch(SQLException err){

        }
        finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        
    //Create/Write log file
        if(Files.exists(Paths.get("../Login_Log.txt"))){
        //Append login attempt to existing log file
            System.out.println("File exists and login attempt appended to log");
            
        }
        else{
        //Create file and add login attempt to new log file
            System.out.println("File created and login attempt logged");
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
//Set mainApp to the main application.
    public void setMainApp(AbsoluteSchedule mainApp) {
        this.mainApp = mainApp;
    }
    
}
