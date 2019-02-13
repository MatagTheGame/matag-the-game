import React, {Fragment, PureComponent} from 'react'
import {connect} from 'react-redux'

class Message extends PureComponent {

  render() {
    if (this.props.message) {
      return (
        <div id="message">
          { this.props.message }
        </div>
      )
    } else {
      return <Fragment />
    }
  }
}

const mapStateToProps = state => {
  return {
    message: state.message
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Message)
