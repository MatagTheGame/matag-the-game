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
    
Or only JavaScript tests with:

    yarn test 

### Tests info

There are two types of java tests **application** and **integration**.

 * **integration**: small tests that cover few Java classes
 * **application**: big tests that cover entire features end to end (Java and JavaScript)

#### Run application tests

Application tests span the entire SpringBootApplication and open browser to hit the server.
They run against HtmlUnit on CI but can be run against ChromeDriver locally.

You may want to change the following options in IntelliJ for all of your tests:
 - Run -> Edit Configuration -> Templates -> JUnit -> VM Options

To run them against ChromeDriver change JUnit VM options in IntelliJ configuration adding:

    -Dwebdriver.chrome.driver=/path/to/your/chromedriver

If you want to use a chromedriver version with some extensions (e.g. redux tools)

    -Dwebdriver.chrome.userDataDir=/path/to/an/empty/folder

Then put a breakpoint on some test and while is executing open a new tab and install everything you want.
Next time the test will rerun will use that same profile.

Tests are by default executed 3 times in case of failures.
When running in IntelliJ is good to add as well:

    -Dtest.gameSetup.retries=1

### Run the application locally

Startup the app as spring boot

    # from intellij or with
    mvn spring-boot:run -Dserver.port=8080 -Dmatag.admin.url=http://localhost:8080 -Dmatag.admin.password=password


It is possible to run `game` app with `test` profile.
This will allow to initialise the game with a custom status (cards in any area) as defined in `ProdInitTestService`.
Furthermore this allows to access a test game without having authentication at:
 - http://localhost:8080/ui/test-game
