import get from 'lodash/get'
import React, {Component} from 'react'
import {connect} from 'react-redux'
import PropTypes from 'prop-types'
import './statusMessage.scss'

class StatusMessage extends Component {
  getMessage() {
    if (this.props.statusMessage && this.props.statusMessage.indexOf('Win!') > 0) {
      return <>{this.props.statusMessage} <a href={this.props.adminUrl}>Go back to admin.</a></>
    } else {
      return this.props.statusMessage
    }
  }

  render() {
    return (
      <div id='status-message'>
        {this.getMessage()}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    statusMessage: get(state, 'userInterface.statusMessage', ''),
    adminUrl: get(state, 'player.gameConfig.adminUrl', '')
  }
}

StatusMessage.propTypes = {
  statusMessage: PropTypes.string.isRequired
}

export default connect(mapStateToProps)(StatusMessage)