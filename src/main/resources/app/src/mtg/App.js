import React, {Fragment, PureComponent} from 'react';
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'
import Message from './Message'
import OpponentHand from './Opponent/OpponentHand'
import PlayerHand from './Player/PlayerHand'
import OpponentLibrary from './Opponent/OpponentLibrary'
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
            <div id="opponent-land-area">
              <div className="card card-1"/>
              <div className="card card-1 tapped"/>
              <div className="card card-2 tapped"/>
            </div>
            <PlayerLibrary/>
            <div id="player-land-area">
              <div className="card card-1" onClick={this.test}/>
              <div className="card card-1"/>
              <div className="card card-2"/>
              <div className="card card-2 tapped"/>
              <div className="card card-2 tapped"/>
            </div>
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
