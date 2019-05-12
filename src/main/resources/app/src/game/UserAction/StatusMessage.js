import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import PropTypes from 'prop-types'

class StatusMessage extends PureComponent {
  render() {
    return (
      <div id='status-message'>
        {this.props.statusMessage}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    statusMessage: state.statusMessage
  }
}

StatusMessage.propTypes = {
  statusMessage: PropTypes.string
}

export default connect(mapStateToProps)(StatusMessage)