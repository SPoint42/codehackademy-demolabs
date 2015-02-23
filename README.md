# Description

This project is a demonstration application for the sessions 3 and 7 of the "Code Hackademy" event: 

http://www.codehackademy.lu/#schedule

> The application try to be, as much as possible, a real application with application security taken in account. The reaminder vulnerabilites come from an possible oversight from DEV team.

This application contains the vulnerabilities below:

* XSS (DOM + Stored),

* CSRF,

* XXE,

* No Content Security Policy,

* Dependency with a known CVE,

* Take SessionID from client cookie and trace association between User and SessionID in application log,

* Use "referer" HTTP request header information for authorization decision,

* Use weak hash algorithm for password hashing.

> Search for tag [EPLVULN] marker in sources files in order to spot vulnerability location.

# Application stack

Project is Maven based and developed using Eclipse Luna IDE.

Front end is based on:

* Bootstrap,

* AngularJS,

* JQuery.


Back end is based on:

* Spring Framework,

* Spring MVC (for REST services),

* Spring Security,

* Spring JDBC templates + H2 embedded database for persistence,

* JAXB for XML serialization/deserialization.


JavaEE 6 web application using JavaSE 7. 


# Data storage

Application use an embedded H2 database that is created and initialized at application startup using the file **db-setup.sql**.


# Commands

## Package application (WAR file)

```
mvn clean package
```

## Run application locally

```
mvn clean tomcat7:run-war
```

Application is then started on local URL **http://localhost:9097/chd/index.jsp**.


# Build

Application is built on a Jenkins instance hosted by [CloudBees](https://www.cloudbees.com) and the resulting artifact (WAR file) is available [here](https://righettod.ci.cloudbees.com/job/CodeHackademyDemoLabs/lastSuccessfulBuild/artifact/target/chd.war) 

Last build status:

[![Build Status](https://righettod.ci.cloudbees.com/buildStatus/icon?job=CodeHackademyDemoLabs)](https://righettod.ci.cloudbees.com/job/CodeHackademyDemoLabs/)

> Build status "Unstable" is normal because we explicilty have a unit test that fail in order to show benefits of unit tests dedicated to application security testing.

# Trigger vulnerabilities

## XSS DOM

Use the URL below to trigger the DOM XSS:

```
http://localhost:9097/chd/index.jsp#/profile?t=<script>alert(1);</script>

``` 

## XSS Stored

In **registration form** or **update motivation option in profile view**, set XSS payload into field **Motivation** and after, login using the new student account.

## XXE

Follow the step below:

1. Log as admin (admin are defined into the SQL file mentioned above), the default student account specified into login form is admin then you can use it,
2. Go into "**Administer**" section and import sample XML provided in file **xxe-sample-filled.xml** using the button **Import**,
3. Login using the new imported student,
4. See XXE results into "**Motivation**" area.

> You can also use the "non Single Page Application" import form style by clicking on button "Import Non SPA" on "Administer" page.

Use [CDATA](http://www.w3schools.com/xml/xml_cdata.asp) XML tag to inject Javascript, example:

``` 
<?xml version="1.0" encoding="UTF-8"?>
<Students>
	<Student>
		<Email>a@a.com</Email>
		<LastnameFirstname>XXE test user</LastnameFirstname>
		<Password>azertyuiop</Password>
		<Motivation><![CDATA[ <script>alert(24);</script> ]]></Motivation>
	</Student>
</Students>	
``` 



# Note about java package naming

* SAL : Service Access Layer

* DAL : Data Access Layer

* UT : Unit Tests

* SUT : Security Unit Tests 
