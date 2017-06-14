/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule;

import absoluteschedule.View_Controller.LogInController;
import absoluteschedule.View_Controller.MainViewController;
import static java.awt.SystemColor.window;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 *
 * @author NicR
 */
public class AbsoluteSchedule extends Application {
    
//Instance Variables
    Stage window;
    
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
    
}
