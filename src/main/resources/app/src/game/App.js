import React, {Component, Fragment} from 'react'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import stompClient from './WebSocket'
import Hand from './Hand/Hand'
import Library from './Library/Library'
import Battlefield from './Battlefield/Battlefield'
import Graveyard from './Graveyard/Graveyard'
import TurnPhases from './Turn/TurnPhases'
import PlayerInfo from './PlayerInfo/PlayerInfo'
import MaximizedCard from './Card/MaximizedCard'
import Stack from './Stack/Stack'
import {UserInterface} from './UserInterface/UserInterface'


class App extends Component {
  componentDidMount() {
    stompClient.init(this.props.receive)
  }

  render() {
    return (
      <Fragment>
        <MaximizedCard/>
        <UserInterface/>
        <Hand type='opponent'/>
        <Hand type='player'/>
        <div id="table">
          <Library type='opponent'/>
          <Battlefield type='opponent'/>
          <Graveyard type='opponent'/>
          <PlayerInfo type='opponent'/>
          <TurnPhases/>
          <Stack/>
          <Library type='player'/>
          <Battlefield type='player'/>
          <Graveyard type='player'/>
          <PlayerInfo type='player'/>
        </div>
      </Fragment>
    )
  }
}

const mapStateToProps = () => {
  return {}
}

const mapDispatchToProps = dispatch => {
  return {
    receive: bindActionCreators((event) => event, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(App)
