/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author NicR
 */
public class SQLManage {

//Instance Variables
    private static String dbType = "mysql";
    private static String url = "52.206.157.109/U04H1H";
    private static String userName = "U04H1H";
    private static String password = "53688238168";
    
//Statment - Update
    public static String getConnText() throws SQLException{
            System.out.println("Connections to SQL DB Opened");
            String text = "jdbc:"+ dbType + "://" + url + ", " + userName + ", " + password;
        return text;
    }
    
//Statement - Query 
    public void stopConn(Connection conn) throws SQLException{
        try{
            conn.close();
            System.out.println("Connection to SQL DB closed");
        }
        catch(SQLException err){
            err.printStackTrace();
        }
    }
}