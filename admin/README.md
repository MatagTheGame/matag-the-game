# Admin

This application is taking care of user management and deck building.
It is deployed at https://matag-admin.herokuapp.com/


## Requisites

 * Install JDK 1.8
 * Install [maven](https://maven.apache.org/download.cgi)
 * Install [yarn 1.21.1(https://classic.yarnpkg.com/en/docs/install)
 * Install [node v12.12.0](https://nodejs.org/en/download/)
 
The application connects to a [postgresql](https://www.postgresql.org/) database.
You can install one locally and then run the [schema.sql](src/main/resource/schema.sql)

## Development

The application is written using:
 * Java ([Spring](https://spring.io/))
  * Javascript ([React](https://reactjs.org/) + [Redux](https://redux.js.org/))

The use of an IDE like [IntelliJ](https://www.jetbrains.com/idea/download/) will help much during development.
(Community edition is available).


### Build

Build java:

    mvn install
    
Build js:

    yarn install
    yarn watch
    
(To have more helps with imports click on the js folder and mark it as resource root)
    

## Tests

### Run Tests locally

You can run all tests with:

    mvn test

Tests mock database interaction so you should be able to code without it.


### Run the application locally

Startup the app as spring boot

    # from intellij or with
    mvn spring-boot:run -DDB_URL=<db_url> -DDB_NAME=<db_name> -DDB_USERNAME=<db_username> -DDB_PASSWORD=<db_password>
    
(The values above depend on your configuration.)
