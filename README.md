# AbsoluteSchedule ReadME
## SQL Based scheduling program to manage user appointments with customers.

1. Contents of this File
2. Introduction
3. Requirements
4. Installation
5. Configuration
6. Program Structure
7. Troubleshooting
8. FAQ
9. About

## Introduction
The Absolute Scheduling software package enables the creation and maintenance of an inter-location scheduling platform. This platform relies on the core Java software package, MySQL database and the internet.

This package is setup to allow a set of users to: 
    -add/edit customers they will meet
    -create/edit appointments with customers
    -generate reports to analyze appointment trends

Additional functionality can be added if needed. 

## Requirements
    -Java 8
    -NetBeans IDE
    -MySQL Database
    -Internet Connection

## Installation
1. To install this project you will need the NetBeans IDE in order to change MySQL DB info so it can be used.
2. NetBeans download and installation instructions can be found here.
3. Once NetBeans is installed you need to open the project from where it was saved.
4. Once the project is open you must configure it with the steps below.
5. Build and Deploy to end users as needed.
6. That’s it.

## Configuration
    -Setup MySQL Database
        -Must have local/hosted MySQL database in order to use
        -Can create a new DB with provided schema or load existing DB into MySQL DB
    -Configure package to connect to database
        -Change connection information in SQLManage class on line 28 to your MySQL DB info
    -Add users
        -There are 3 users currently in included DB
        -More can be added if needed
    -Deploy to users
        -Build the program in NetBeans
        -Provide JAR file to end users to use

## Program Structure
###### UI Classes (View/Controller)
    -Login
    -Main
    -Customer
    -Calendar
    -Report

###### Model
    -Customer
        -Class houses methods to pull, store, and manipulate data pulled from the SQL table needed to manage customer information and view it
    -Calendar
        -Class houses methods to pull, store, and manipulate data pulled from the SQL table needed to manage appointments and view them in the various screens
    -Report
        -Class houses methods to pull, store and manipulate data pulled from the SQL table needed to generate reports on demand to analyze productivity of scheduling
    -UI Elements
        -Class houses methods to pull and store data needed to update the dynamic UI fields to enrich the user experience
            -Date/Time on main screen
            -Counts of appointments for day, week, month to give an overview of upcoming appointments at a glance
            -Calendar View
                -The dates will be auto updated to current week or month and highlight the current day for efficiency
                -The view will update if user goes back or forward a month

###### Alert
    -Alerts are created through one of two static methods in the main AbsolutSchedule class.
        -One is for standard informational alerts
        -The other is for confirmation alerts when going back to main screen or exiting program

###### Reminder
    -Reminders are checked for every minute. This is in the ListManage class
    -They create a standard informational popup for the logged in user when an appointment is within the next 15 minutes

###### Helper Files
    -Resource Bundle Helper
        -This class provides a method to load the resourcebundle for localization
    -SQL Connection Management
        -This class sets up connection to SQL database so queries and updates can be submitted easily.
        -It also provides a Lambda to create a prepared statement easily.
    -List Manager
        -This class provides methods to manage the observable arraylists for customers and calendar events
    -SQL Schema
        -Included in this folder is an SQL file that contains the schema if a new DB is being built for this program

###### Localization
    -There are two languages supported
        -English, USA
        -Spanish, USA
    -Alerts, errors, popups will be translated based on user’s localization

## Troubleshooting
None at this time. Will update as feedback is provided.

## FAQ
None at this time. Will update as questions come in.

## About
Nic Reichelt - Project Planner/Software Developer

This project was created for an advanced Java class as part of my Bachelor's degree program.

Western Governors University provided the requirements for the package and I designed and implemented the program.

This program took approximately 5 weeks to design, implement, test and deploy.
