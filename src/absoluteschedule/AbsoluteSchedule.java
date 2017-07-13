/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule;

import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
import absoluteschedule.View_Controller.LogInController;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author NicR
 */
public class AbsoluteSchedule extends Application {
    
//Instance Variables
    Stage window;
    private final ResourceBundle localization = loadResourceBundle();
    
//RootLayout
    public void initLogin() throws IOException{
    //Load parts overview
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AbsoluteSchedule.class.getResource("View_Controller/LogIn.fxml"));
        AnchorPane LoginLoader = (AnchorPane) loader.load();
    
    //Setting the scene
        Scene scene = new Scene(LoginLoader);
        
    //Showing the scene on the stage
        window.setScene(scene);
        window.show();
    }
    public void showLogin() throws IOException{
    // Load Login screen.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AbsoluteSchedule.class.getResource("View_Controller/LogIn.fxml"));
        AnchorPane LoginLoader = (AnchorPane) loader.load();
        
    //Give Controller access to the Main Application
        LogInController controller = loader.getController();
        controller.setMainApp(this);
    }
    
    //Starting Method
    @Override
    public void start(Stage primaryStage) throws SQLException, IOException {
    
    //Setting the stage
        window = primaryStage;
        window.setTitle("Absolute Schedule");
        
        initLogin();
        showLogin();
        
    //SQL connection test
        Connection conn = DriverManager.getConnection("jdbc:mysql://52.206.157.109/U04H1H", "U04H1H","53688238168");
        System.out.println("Connection: " + conn + " was successful.");
        conn.close();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){

        launch(args);
    }
    
//Create Popups
    public static void createStandardAlert(String alertMessage, String alertHeader, String alertTitle){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertMessage);
        alert.showAndWait();
    }
    public static Optional<ButtonType> createConfirmAlert(String alertMessage, String alertHeader, String alertTitle){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle(alertTitle);
        alert.setHeaderText(alertHeader);
        alert.setContentText(alertMessage);
        Optional<ButtonType> result = alert.showAndWait();
        
        return result;
    }
    
//Cancellation and Exit Alerts
    public void setMain(ActionEvent event) throws IOException{
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
    public void cancelHandler(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirm!");
        alert.setHeaderText("Cancel Confirmation!");
        alert.setContentText(localization.getString("cancel_confirm"));
        Optional<ButtonType> confirm = alert.showAndWait();
        
        confirm.ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    setMain(event);
                } catch (IOException ex) {
                    Logger.getLogger(AbsoluteSchedule.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}