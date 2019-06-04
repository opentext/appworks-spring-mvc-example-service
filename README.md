# AppWorks Spring MVC Service Example 16.5.1

The project contains example code for an AppWorks 16.5+ Service that uses the
Spring MVC framework and does not contain a web.xml file. 

Please see the following link for general information of AppWorks Services.

https://developer.opentext.com/awd/resources/articles/15239948/developer+guide+opentext+appworks+16+service+development+kit.

## Building the service

This is a Java 8 project and uses Apache Maven to perform the build. Once built using this
project and will produce a zip `appworks-spring-mvc-service_16.5.1.zip` in the `/target` directory that can be deployed
to an AppWorks Gateway 16.5+ instance. All you need to do is run `mvn clean package`.

Please review the pom.xml for the build process, but more importantly the service's code itself for information on
how the AppWorks SDK works and how to build a minimal but functional AppWorks Spring MVC service.

## Deploying the service

This service makes use of features introduced in AppWorks 16.5.0 so deploy this service via the AppWorks 16.5+ 
administration UI. Once installed ensure it is enabled via the UI, and then you should be able to make a request 
to `http://{yourhost}:8080/appworks-spring-mvc-service/api/welcome`, and this should be served by the 
`com.appworks.web.xml.less.ExampleController` Spring MVC controller.

## SDK Examples API

This service's API exposes a number of endpoints that exercise the SDK clients and their functionality. They are 
listed below, and are all called via a HTTP `GET`.

### Get known runtimes

`http://{yourhost}:8080/appworks-spring-mvc-service/api/runtimes`

### Get known providers

This endpoint will create a test Trusted Provider for this service if there is not one already, and will return
the known set of Trusted Providers.

`http://{yourhost}:8080/appworks-spring-mvc-service/api/providers`

### Send an email

A predefined email will be sent to the specified user, if the AppWorks Gateway mail settings have been configured. 
The synchronous and asynchronous mail methods are exposed via the query parameters. There are three mandatory
parameters, `to`, `from`, and `async`.

`http://{yourhost}:8080/appworks-spring-mvc-service/api/mail?to=test@gmail.com&from=rhyse@opentext.com&async=true`

### Get the service setting

This endpoint will create or update a setting, and will then return all this services known settings.

`http://{yourhost}:8080/appworks-spring-mvc-service/api/settings`

### Send a notification

This endpoint allows users to send a simple predefined message as a push notification or web notification to
a specific user. The `username` query parameter must be provided, and if the `push` query parameter is set to `true`, 
then the `targetApp1 must also be supplied. 

To send a push notification successfully a real username of a user that has connected to the Gateway previously 
using a push-enabled AppWorks client (the standard AppWorks Android or iOS client for example). 

`http://{yourhost}:8080/appworks-spring-mvc-service/api/notifications?username=someUser&push=true&targetApp=starter` 

