# Game

This application is taking care of playing matches between players.

See [Matag: The Game](https://github.com/MatagTheGame/matag-the-game/wiki) wiki


## Automated Build

![Java CI with Maven](https://github.com/MatagTheGame/matag-the-game/workflows/Java%20CI%20with%20Maven/badge.svg)
 - https://github.com/MatagTheGame/matag-the-game/actions
 

## Requisites

Read [Requisites](https://github.com/MatagTheGame/game/wiki/Requisites)

## Development

The application is written using:
 * Java ([Spring](https://spring.io/))
 * Javascript ([React](https://reactjs.org/) + [Redux](https://redux.js.org/))


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
They run against Chrome (which must be installed on the machine).

Change the following options in IntelliJ for all of your tests:
 - Run -> Edit Configuration -> Templates -> JUnit -> VM Options

If you want to run headless (which is how CI runs) set:

    -Dwebdriver.chrome.headless=true

If you want to use a Chrome with some extensions (e.g. redux tools)

    -Dwebdriver.chrome.userDataDir=/path/to/an/empty/folder

Then put a breakpoint on some test and while is executing open a new tab and install everything you want.
Next time the test will rerun will use that same profile.

### Run the application locally

Startup the app as spring boot

    # from intellij or with
    mvn spring-boot:run -Dspring-boot.run.profiles=dev


It is possible to run `game` app with `test` profile.
This will allow to initialise the game with a custom status (cards in any area) as defined in `ProdInitTestService`.
Furthermore this allows to access a test game without having authentication at:
 - http://localhost:8080/ui/test-game
