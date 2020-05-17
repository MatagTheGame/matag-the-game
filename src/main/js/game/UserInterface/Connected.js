import React, {Component} from 'react'
import stompClient from 'game/WebSocket'
import {connect} from 'react-redux'
import get from 'lodash/get'

const HEALTCHECK_TIMEOUT = 3000

class Connected extends Component {
  constructor(props) {
    super(props)
    this.healthcheck = this.healthcheck.bind(this)
    this.state = {lastHealthcheckSent: new Date()}
  }

  componentDidMount() {
    this.interval = setInterval(this.healthcheck, HEALTCHECK_TIMEOUT)
  }

  componentWillUnmount() {
    clearInterval(this.interval)
  }

  healthcheck() {
    stompClient.sendHeartbeat()
    this.setState({lastHealthcheckSent: new Date()})
  }

  displayStatus() {
    const lastHealthcheckReceivedPlusOffset = new Date(this.props.lastHealthcheckReceived.getTime() + HEALTCHECK_TIMEOUT)
    if (lastHealthcheckReceivedPlusOffset.getTime() > this.state.lastHealthcheckSent.getTime()) {
      return 'connected'
    } else {
      return 'disconnected'
    }
  }

  render() {
    return <div id='connection-status' className={this.displayStatus()} title={this.displayStatus()} />
  }
}

const mapStateToProps = state => {
  return {
    lastHealthcheckReceived: new Date(get(state, 'userInterface.websocket.lastHealthcheckReceived', new Date()))
  }
}

const mapDispatchToProps = () => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Connected)
