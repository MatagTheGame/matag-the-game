# Mtg

MTG.



## Development

The application is written using:
 * Java (Spring)
 * Javascript (React + Redux)



### Requisites

 * Install JDK 1.11
 * Install maven
 * Install yarn

### Build & Run

#### Build on Travis

[![Build Status](https://travis-ci.com/antonioalonzi/open-games.svg?branch=master)](https://travis-ci.com/antonioalonzi/open-games)

#### Build locally
    
Build js:

    yarn install
    yarn build-dev

Run tests:
    
    yarn test

Run from command line:

    mvn spring-boot:run


## Deployment

Application is deployed at: https://open-games.herokuapp.com/

The deploy happens automatically after each commit on master if the build passed. 

 * Heroku: https://dashboard.heroku.com/apps/aa-mtg
 * Travis: https://travis-ci.com/antonioalonzi/mtg
