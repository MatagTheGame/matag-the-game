import React, {Fragment, PureComponent} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import stompClient from './WebSocket'
import Message from './UserAction/Message'
import StatusMessageComponent from './UserAction/StatusMessageComponent'
import Hand from './Hand/Hand'
import Library from './Library/Library'
import Battlefield from './Battlefield/Battlefield'
import Graveyard from './Graveyard/Graveyard'
import TurnPhases from './Turn/TurnPhases'
import PlayerInfo from './PlayerInfo/PlayerInfo'
import UserActionComponent from './UserAction/UserActionComponent'


class App extends PureComponent {
  componentDidMount() {
    stompClient.init(this.props.receive)
  }

  render() {
    return (
      <Fragment>
        <UserActionComponent />
        <StatusMessageComponent />
        <Hand type='opponent'/>
        <Hand type='player'/>
        <div id="table">
          <div>
            <Library type='opponent'/>
            <Battlefield type='opponent'/>
            <Graveyard type='opponent'/>
            <PlayerInfo type='opponent'/>
            <TurnPhases/>
            <Library type='player'/>
            <Battlefield type='player'/>
            <Graveyard type='player'/>
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
