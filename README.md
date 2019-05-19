# Mtg

MTG game.



## Development

The application is written using:
 * Java (Spring)
 * Javascript (React + Redux)


### Tests info

There are two types of tests **application** and **integration**.

 * **integration** tests are smaller tests that test small bits of Java code
 * **application** tests are bigger tests that test entire features end to end

#### Run application tests

Application tests span the entire SpringBootApplication and open browser to hit the server.

They run against HtmlUnit on Travis but can be run against ChromeDriver locally.

To run them against ChromeDriver change the default JUnit IntelliJ configuration adding the following in the VM arguments:

    -Dwebdriver.chrome.driver=/your/chromedriver/installation/folder 


### Requisites

 * Install JDK 1.8
 * Install maven
 * Install yarn

### Build & Run

#### Build on Travis

[![Build Status](https://travis-ci.com/antonioalonzi/mtg.svg?branch=master)](https://travis-ci.com/antonioalonzi/mtg)

#### Build locally
    
Build js:

    yarn install
    yarn watch
    
Startup the app as spring boot

    # from intellij or with
    mvn spring-boot:run
    
It is possible to run with `test` profile in order to start custom decks rather than random (see `InitTestService`)

Run tests:
    
    yarn test
    mvn test

## Deployment

Application is deployed at: https://aa-mtg.herokuapp.com/

The deploy happens automatically after each commit on master if the build passed. 

 * Heroku: https://dashboard.heroku.com/apps/aa-mtg
 * Travis: https://travis-ci.com/antonioalonzi/mtg
