import React, {Fragment, PureComponent} from 'react';
import {connect} from 'react-redux'
import OpponentHand from './OpponentHand'
import PlayerHand from './PlayerHand'

class App extends PureComponent {
  render() {
    return (
      <Fragment>
        <OpponentHand />
        <PlayerHand />
        <div id="table">
          <div>
            <div id="opponent-library">
              <div className="card card-0"/>
            </div>
            <div id="opponent-land-area">
              <div className="card card-1"/>
              <div className="card card-1 tapped"/>
              <div className="card card-2 tapped"/>
            </div>
            <div id="player-library">
              <div className="card card-0"/>
            </div>
            <div id="player-land-area">
              <div className="card card-1"/>
              <div className="card card-1"/>
              <div className="card card-2"/>
              <div className="card card-2 tapped"/>
              <div className="card card-2 tapped"/>
            </div>
          </div>
        </div>
        <div id="message" />
      </Fragment>
    )
  }
}

const mapStateToProps = state => {
  return {
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)