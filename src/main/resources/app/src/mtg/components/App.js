import React from 'react'
import {Fragment, PureComponent} from 'react';
import {connect} from 'react-redux'
import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

class App extends PureComponent {
  constructor(props) {
    super(props)
  }

  componentDidMount() {
    // TODO move this code into stompReducer - initState
    let socket = new SockJs('/mtg-ws')
    let stompClient = Stomp.over(socket)

    stompClient.connect({}, () => {
      this.sessionId = socket._transport.url.split('/')[5]

      stompClient.subscribe('/topic/events', (event) => {
        const eventBody = JSON.parse(event.body)
        console.log(eventBody)
      })

      stompClient.subscribe('/user/' + this.sessionId + '/events', (event) => {
        const eventBody = JSON.parse(event.body)
        console.log(eventBody)
      })

      stompClient.send('/api/init', {}, '{}')
    })
  }

  render() {
    return (
      <Fragment>
        <div id="opponent-hand">
          <div id="1" className="card card-0"/>
          <div id="2" className="card card-0"/>
          <div id="3" className="card card-0"/>
          <div id="4" className="card card-0"/>
        </div>
        <div id="player-hand">
          <div id="11" className="card card-1"/>
          <div id="12" className="card card-2"/>
          <div id="13" className="card card-3"/>
        </div>
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