# Description

This project is a demonstration application for the sessions 3 and 7 of the "Code Hackademy" event: http://www.codehackademy.lu/#schedule

Project is Maven based.

This application contains the vulnerabilities below:

* XSS (DOM + Stored),

* CSRF,

* XXE,

* No Content Security Policy.

# Data storage

Application use an embedded H2 database that is created and initialized at application startup using the file **db-setup.sql**.


# Commands to package or run locally the application

## Package application (WAR file)

```
mvn clean package
```

## Run application locally

```
mvn clean tomcat7:run-war
```

Application is then started on local URL **http://localhost:9097/chd/index.jsp**.


# Use vulnerabilities

## XSS

### DOM

Use the URL below to trigger the DOM XSS:

```
http://localhost:9097/chd/index.jsp#/profile?t=<script>alert(1);</script>

``` 

### Stored

In registration form, set XSS payload into field "Motivation" and after login using the new student account.

### XXE

Follow the step below:

1. Log as admin (admin are defined into the SQL file mentioned above), the default student account specified into login form is admin the you can use it,
2. Go into "**Administer**" section and import sample XML provided (its contains a XXE sample),
3. Login using the new imported student,
4. See XXE results into "**Motivation**" area.


# Note about java package naming

* SAL : Service Access Layer

* DAL : Data Access Layer
