# Mtg

Early Access version of an MTG-like game implementation.
Please note that we are not affiliated in any way with the MTG creators, nor we claim any copyright over their game or art assets.

Try it out: https://aa-mtg-game.herokuapp.com/ui/game

In case you are the only one online, you can play against yourself by opening two browser sessions (windows/tabs) with the address above.

For any questions or comments, please contact antonioalonzi85@gmail.com.

![Snapshot](README_SNAPSHOT.png)



# Architecture

The application is divided in modules:
- [game](game/README.md): responsible for playing matches.
- [admin](admin/README.md): responsible for user and decks management (not implemented yet).



# Backlog

The backlog is managed in Kanban style at:
 - https://github.com/antonioalonzi/mtg/projects/1
 
Stories labelled `with guidelines` are very well described and easy to be picked up for people who want to start
contributing and learning the project.


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

Copyright © 2019,2020 Antonio Alonzi and [contributors](https://github.com/antonioalonzi/mtg/graphs/contributors)

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
