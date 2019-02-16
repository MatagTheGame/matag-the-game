import React, {Fragment, PureComponent} from 'react';
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'
import Message from './Message'
import OpponentHand from './Opponent/OpponentHand'
import OpponentLandArea from './Opponent/OpponentLandArea'
import OpponentLibrary from './Opponent/OpponentLibrary'
import PlayerHand from './Player/PlayerHand'
import PlayerLandArea from './Player/PlayerLandArea'
import PlayerLibrary from './Player/PlayerLibrary'


class App extends PureComponent {
  componentDidMount() {
    let socket = new SockJs('/mtg-ws')
    let stompClient = Stomp.over(socket)

    stompClient.connect({}, () => {
      const sessionId = socket._transport.url.split('/')[5]

      stompClient.subscribe('/topic/events', (event) => {
        const eventBody = JSON.parse(event.body)
        this.props.receive(eventBody)
      })

      stompClient.subscribe('/user/' + sessionId + '/events', (event) => {
        const eventBody = JSON.parse(event.body)
        this.props.receive(eventBody)
      })

      stompClient.send('/api/init', {}, '{}')
    })
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
            <PlayerLibrary/>
            <PlayerLandArea/>
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
