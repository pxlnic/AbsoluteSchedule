/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Model;

import static absoluteschedule.Helper.ResourcesHelper.loadResourceBundle;
import java.util.ResourceBundle;

/**
 *
 * @author NicR
 */
public class Report {
    
//Instance Variables
    private String reportName;
    private String reportTimePeriod;
    private String reportDay;
    private String reportConsultant;
    private String reportType;
    private String reportItem;
    private static ResourceBundle localization = loadResourceBundle();
    
//Constructor
    public Report(){
        reportName = "set name";
        reportTimePeriod = "set tmeframe";
        reportDay = "set day";
        reportConsultant = "set consultant";
        reportType = "set type";
        reportItem = "set item";
    }
    
//Setters and getters
    //Report Name
    public void setReportName(String name){
        reportName = name;
    }
    public String getReportName(){
        return reportName;
    }
    //Report Timeframe
    public void setReportTimePeriod(String time){
        reportTimePeriod = time;
    }
    public String getReportTimePeriod(){
        return reportTimePeriod;
    }
    //Report Day
    public void setReportDay(String day){
        reportDay = day;
    }
    public String getReportDay(){
        return reportDay;
    }
    //Report Consultant
    public void setReportConsultant(String consultant){
        reportConsultant = consultant;
    }
    public String getReportConsultant(){
        return reportConsultant;
    }
    //Report Type
    public void setReportType(String type){
        reportType = type;
    }
    public String getReportType(){
        return reportType;
    }
    //Report Item
    public void setReportItem(String item){
        reportItem = item;
    }
    public String getReportItem(){
        return reportItem;
    }
    
//Check if entry is valid - date, startHour, startMin, endHour, endMin, allDay, customerName, consultantName, location, title, desc
    public static String isEntryValidView(String message, String testType, String testYear, String testMonth){
   //Test Month
        try{
            if(testMonth.equals("")){
                message = message + localization.getString("report_month");
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("report_month");
        }
        
    //Test Year
        try{
            if(testYear.equals("")){
                message = message + localization.getString("report_year"); 
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("report_year");
        }

    //Test Report Type
        try{
            if(testType.equals("")){
                message = message + localization.getString("report_type");
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("report_type");
        }
        
    //Return Error message
        return message;
    }
    
//Check if entry is valid - date, startHour, startMin, endHour, endMin, allDay, customerName, consultantName, location, title, desc
    public static String isEntryValidSave(String message, String testType, String testYear, String testMonth, String testTitle, String testNotes){
   //Test Month
        try{
            if(testMonth.equals("")){
                message = message + localization.getString("report_month");
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("report_month");
        }
        
    //Test Year
        try{
            if(testYear.equals("")){
                message = message + localization.getString("report_year"); 
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("report_year");
        }

    //Test Report Type
        try{
            if(testType.equals("")){
                message = message + localization.getString("report_type");
            }
        }
        catch(NullPointerException e){
            message = message + localization.getString("report_type");
        }
        
    //Test Title
        if(testTitle.equals("")){
            message = message + localization.getString("report_title");
        }
    //Test Notes
        if(testNotes.equals("")){
            message = message + localization.getString("report_notes");
        }
    
    //Return Error message
        return message;
    }
}
