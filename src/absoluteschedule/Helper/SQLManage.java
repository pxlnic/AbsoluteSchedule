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
    private Connection conn;
    private String dbType = "mysql";
    private String url = "52.206.157.109/U04H1H";
    private String userName = "U04H1H";
    private String password = "53688238168";
    private Statement stmnt;
    
//Statment - Update
    public void sqlUpdate(String sqlUpdate) throws SQLException{
        try{
            conn = DriverManager.getConnection("jdbc:"+ dbType + "://" + url + ", " + userName + ", " + password);
            stmnt = conn.createStatement();
            String sql = sqlUpdate;
            System.out.println(sql);
            stmnt.executeUpdate(sql);
        }
        catch(SQLException err){
            err.printStackTrace();
        }
    }
    
//Statement - Query
    public ResultSet sqlQuery(String sqlQuery){
        try{
            conn = DriverManager.getConnection("jdbc:"+ dbType + "://" + url + ", " + userName + ", " + password);
            stmnt = conn.createStatement();
            String sql = sqlQuery;
            System.out.println(sql);
            stmnt.executeUpdate(sql);
        }
        catch(SQLException err){
            err.printStackTrace();
        }
        return null;
    }
}
