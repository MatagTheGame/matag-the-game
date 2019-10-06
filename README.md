# Mtg

Early access implementation of an MTG game accessible at: https://aa-mtg.herokuapp.com
(Most probably there will be nobody online, but you can try it out playing against yourself by opening to tabs at the address above). 

For any questions please contact: antonio.aa.mtg@gmail.com


## Requisites

 * Install JDK 1.8
 * Install [maven](https://maven.apache.org/download.cgi)
 * Install [yarn](https://yarnpkg.com/lang/en/docs/install)
 * Download [Chromedriver](https://chromedriver.chromium.org/downloads)


## Development

The application is written using:
 * Java ([Spring](https://spring.io/))
 * Javascript ([React](https://reactjs.org/) + [Redux](https://redux.js.org/))

The use of an IDE like [IntelliJ](https://www.jetbrains.com/idea/download/#section=mac) will help much during development.
(Community edition is available).

### Build

Build java:

    mvn install
    
Build js:

    yarn install
    yarn watch


## Tests

### Run Tests locally

You can run all tests with:

    mvn test
    
Or only JavaScript tests with:

    yarn test 

### Tests info

There are two types of java tests **application** and **integration**.

 * **integration**: small tests that cover few Java classes
 * **application**: big tests that cover entire features end to end (Java and JavaScript)

#### Run application tests

Application tests span the entire SpringBootApplication and open browser to hit the server.

They run against HtmlUnit on Travis but can be run against ChromeDriver locally.

To run them against ChromeDriver change JUnit VM options in IntelliJ configuration adding:

    -Dwebdriver.chrome.driver=/your/chromedriver/installation/folder

If you want to use a chromedriver version with some extensions (e.g. redux tools)

    -D"webdriver.chrome.userDataDir=/tmp/Chrome/Mtg/"

Then put a breakpoint on some test and while is executing open a new tab and install everything you want.
Next time the test will rerun will use that same profile.

Tests are by default executed 10 times in case of failures.
When running in IntelliJ is good to add as well:

    -Dtest.gameSetup.retries=1 


## Run the application locally

Startup the app as spring boot

    # from intellij or with
    mvn spring-boot:run

It is possible to run with `test` profile in order to start custom decks rather than random (see `InitTestService`)


## Backlog

The backlog is managed using milestones:

 - https://github.com/antonioalonzi/mtg/milestone/1


## Continuous Integration on Travis

[![Build Status](https://travis-ci.com/antonioalonzi/mtg.svg?branch=master)](https://travis-ci.com/antonioalonzi/mtg)

 * Travis: https://travis-ci.com/antonioalonzi/mtg

## Continuous Deployment on Heroku

Application is deployed at: https://aa-mtg.herokuapp.com/

The deploy happens automatically after each commit on master if the build passed:
 * Heroku: https://dashboard.heroku.com/apps/aa-mtg
 
