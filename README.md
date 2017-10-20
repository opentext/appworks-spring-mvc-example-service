# AppWorks Spring MVC Service Example 16.3.0

The project contains example code for an AppWorks 16.3.0 Service that uses the
Spring MVC framework and does not contain a web.xml file. The service makes use
of the `servlet3service` app type (see app.properties) introduced in AppWorks 16.3.0.

Please see the following link for general information of AppWorks Services.

https://developer.opentext.com/awd/resources/articles/15239948/developer+guide+opentext+appworks+16+service+development+kit.

## Building the service

This is a Java 8 project and uses Apache Maven to perform the build so you will both installed. Once built using this
project and will produce a zip (appworks-service-example_1.0.0.zip) in the `/target` directory that can be deployed
to an AppWorks Gateway 16.2 instance. All you need to do is run `mvn clean package`.

Please review the pom.xml for the build process, but more importantly the service's code itself for information on
how the AppWorks SDK works and how to build a minimal but functional AppWorks Spring MVC service.

## Deploying the service

This service makes use of features introduced in AppWorks 16.3.0 so deploy this service via the AppWorks 16.3.0 
administration UI. Once installed ensure it is enabled via the UI, and then you should be able to make a request 
to `http://{yourhost}:8080/appworks-spring-mvc-service/api/welcome`, and this should be served by the 
`com.appworks.web.xml.less.ExampleController` Spring MVC controller.
