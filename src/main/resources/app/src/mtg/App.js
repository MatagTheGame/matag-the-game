import React, {Fragment, PureComponent} from 'react';
import {connect} from 'react-redux'
import OpponentHand from './OpponentHand'
import PlayerHand from './PlayerHand'
import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'
import {bindActionCreators} from 'redux'

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

  displayMessage() {
    console.log('message: ', this.props.message)
    if (this.props.message) {
      return (
        <div id="message">
          { this.props.message }
        </div>
      )
    }
  }

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
        {this.displayMessage()}
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
