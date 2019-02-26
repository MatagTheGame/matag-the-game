import React, {PureComponent} from 'react'
import {connect} from 'react-redux'

class StatusMessageComponent extends PureComponent {
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

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(StatusMessageComponent)