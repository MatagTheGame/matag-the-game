import get from 'lodash/get'
import React from 'react'
import {connect} from 'react-redux'
import PropTypes from 'prop-types'
import './statusMessage.scss'

function StatusMessage(props) {
  return (
    <div id='status-message'>
      {props.statusMessage}
    </div>
  )
}

const mapStateToProps = state => {
  return {
    statusMessage: get(state, 'userInterface.statusMessage', '')
  }
}

StatusMessage.propTypes = {
  statusMessage: PropTypes.string.isRequired
}

export default connect(mapStateToProps)(StatusMessage)