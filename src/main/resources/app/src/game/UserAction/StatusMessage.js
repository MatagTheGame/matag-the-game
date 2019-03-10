import React, {PureComponent} from 'react'
import {connect} from 'react-redux'

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

export default connect(mapStateToProps)(StatusMessage)