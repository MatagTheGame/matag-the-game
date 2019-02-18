import React, {Fragment, PureComponent} from 'react';
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import stompClient from './WebSocket'
import Message from './Message'
import OpponentHand from './Hand/OpponentHand'
import OpponentLandArea from './LandArea/OpponentLandArea'
import OpponentLibrary from './Library/OpponentLibrary'
import PlayerHand from './Hand/PlayerHand'
import PlayerLandArea from './LandArea/PlayerLandArea'
import PlayerLibrary from './Library/PlayerLibrary'
import PlayerInfo from './PlayerInfo/PlayerInfo'
import OpponentInfo from './PlayerInfo/OpponentInfo'
import TurnPhases from './Turn/TurnPhases'


class App extends PureComponent {
  componentDidMount() {
    stompClient.init(this.props.receive)
  }

  render() {
    return (
      <Fragment>
        <OpponentHand />
        <PlayerHand />
        <div id="table">
          <div>
            <OpponentLibrary/>
            <OpponentLandArea/>
            <OpponentInfo/>
            <TurnPhases/>
            <PlayerLibrary/>
            <PlayerLandArea/>
            <PlayerInfo/>
          </div>
        </div>
        <Message/>
      </Fragment>
    )
  }
}

const mapStateToProps = state => {
  return {
    message: state.message
  }
}

const mapDispatchToProps = dispatch => {
  return {
    receive: bindActionCreators((event) => event, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)
