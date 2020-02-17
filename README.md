# Mtg

Early access implementation of an MTG game accessible at: https://aa-mtg-game.herokuapp.com/ui/game
(Most probably there will be nobody online, but you can try it out playing against yourself by opening to tabs at the address above).

For any questions please contact: antonio.aa.mtg@gmail.com

![Snapshot](README_SNAPSHOT.png)



# Architecture

The application is divided in modules:
 - [admin](admin/README.md): responsible for user and decks management.
 - [game](game/README.md): responsible for playing matches.



# Backlog

The backlog is managed at:
 - https://github.com/antonioalonzi/mtg/milestone/1


# CI/CD

## Continuous Integration on Travis

[![Build Status](https://travis-ci.com/antonioalonzi/mtg.svg?branch=master)](https://travis-ci.com/antonioalonzi/mtg)

 - Travis: https://travis-ci.com/antonioalonzi/mtg

Tests run for all the modules together.
For the `game` module `Regression` tests are skipped. Read module readme for more info.

## Continuous Deployment on Heroku

Applications are continuously deployed on heroku:
 - Admin: https://dashboard.heroku.com/apps/aa-mtg-admin
 - Game: https://dashboard.heroku.com/apps/aa-mtg-game


 ## License

 Copyright © 2019,2020 Antonio Alonzi

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License as published by
 the Free Software Foundation, either version 3 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 GNU General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program. If not, see <http://www.gnu.org/licenses/>.
