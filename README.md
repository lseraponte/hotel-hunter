# Hotel Hunter by Luigi Seraponte

### The target of this application is to use Cupid API endpoints to fetch and store Hotel properties, reviews and translations, allowing users to do operations with the saved data.

### The Project Design

There are two controllers in the application, one is CupidApiController and is used to connect directly with Cupid API:
- Get Hotel by ID
- Get Hotel with Translation
- Get Hotel Reviews

The other one HotelController which offer endpoints to allow operations like:
- Save Hotel
- Get Hotel by ID
- Get Hotel by Name
- Get Hotels by City
- Update Hotel
- Delete Hotel
- Add Hotel Reviews
- Get Hotel Reviews

The HotelController uses DTO received from the API user to create the Entity classes to be stored on the database.

The service layers are:
- Controllers (to handle http requests)
- Services (to deal with DTOs and create entities to be persisted/retrieved)
- Model (The class representation of the data returned from Cupid API as a DTO)
- Repository (The database transaction level)
- DTO (the data format returned from Cupid API)

Plus there are util and config packages.

In order to populate the local database, there is a @PostConstruct method in the HotelStartupService class. This method is triggered as soon as the app is started and pars from a text file the hotel ids available.
One by one then it calls the Cupid API endpoint to get the hotels in English, French and Spanish together with reviews, then it sends this data to the Hotel Hunter APIs persist.

To keep the data updated, there is a cron job in the HotelCronService, so that every hour the data are fetched from Cupid API and the local entities are updated.

### How to run Hotel Hunter

Hotel Hunter is a Spring Boot Java application.
The App code brings in a Maven Wrapper, thus there is no need to have a MAVEN_HOME set on the machine.
To run the application navigate to the root folder of the project from your command line interpreter, then run the following command:
- For Unix-like machines: ```./mvnw spring-boot:run```
- For Windows machines: ```mvnw.cmd spring-boot:run```

The App will run on an embedded Tomcat and is reachable on ```localhost:8080``` when run locally.

Attached a Postman collection/environment to send requests to the APIs.

A Swagger endpoint is available to consult APIs specs here: ```localhost:8080/webjars/swagger-ui/index.html```.

