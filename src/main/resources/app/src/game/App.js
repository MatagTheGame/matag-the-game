import React, {Fragment, PureComponent} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import stompClient from './WebSocket'
import Message from './UserAction/Message'
import PlayerHand from './Hand/PlayerHand'
import PlayerLandArea from './Battlefield/PlayerLandArea'
import PlayerLibrary from './Library/PlayerLibrary'
import PlayerInfo from './PlayerInfo/PlayerInfo'
import TurnPhases from './Turn/TurnPhases'
import UserActionComponent from './UserAction/UserActionComponent'
import StatusMessageComponent from './UserAction/StatusMessageComponent'
import PlayerGraveyard from './Graveyard/PlayerGraveyard'


class App extends PureComponent {
  componentDidMount() {
    stompClient.init(this.props.receive)
  }

  render() {
    return (
      <Fragment>
        <UserActionComponent />
        <StatusMessageComponent />
        <PlayerHand type='opponent'/>
        <PlayerHand type='player'/>
        <div id="table">
          <div>
            <PlayerLibrary type='opponent'/>
            <PlayerLandArea type='opponent'/>
            <PlayerGraveyard type='opponent'/>
            <PlayerInfo type='opponent'/>
            <TurnPhases/>
            <PlayerLibrary type='player'/>
            <PlayerLandArea type='player'/>
            <PlayerGraveyard type='player'/>
            <PlayerInfo type='player'/>
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
