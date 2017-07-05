/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package absoluteschedule.Model;

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
    private String reportConsultandSchedule;
    
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
}
