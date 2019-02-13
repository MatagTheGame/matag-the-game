import React, {Fragment} from 'react'
import {render} from 'react-dom'
import SockJs from 'sockjs-client'
import {Stomp} from '@stomp/stompjs'

class App extends React.Component {
  constructor(props) {
    super(props)
  }

  componentDidMount() {
    let socket = new SockJs('/mtg-ws')
    let stompClient = Stomp.over(socket)
    // TODO save stompClient in redux

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

render(<App/>, document.getElementById('app'))
