import React, {Fragment, Component} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import {bindActionCreators} from 'redux'
import PropTypes from 'prop-types'

class Message extends Component {
  constructor(props) {
    super(props)
    this.handleEscape = this.handleEscape.bind(this)
  }

  handleEscape(event) {
    if (event.key === 'Escape' && this.props.message.closable) {
      this.props.closeMessage()
    }
  }

  componentDidMount() {
    document.addEventListener('keydown', this.handleEscape)
  }

  componentWillUnmount() {
    document.removeEventListener('keydown', this.handleEscape)
  }

  renderCloseButton() {
    if (this.props.message.closable) {
      return <i id='message-close' onClick={this.props.closeMessage} aria-hidden='true'>X</i>
    }
  }

  renderMessageText() {
    return <p id='message-text'>{ this.props.message.text }</p>
  }

  render() {
    if (this.props.message.text) {
      return (
        <Fragment>
          <div id='modal-container' />
          <div id='message'>
            { this.renderCloseButton() }
            { this.renderMessageText() }
          </div>
        </Fragment>
      )
    } else {
      return <Fragment />
    }
  }
}

const closeMessageEvent = () => {
  return {
    type: 'MESSAGE',
    value: {}
  }
}

const mapStateToProps = state => {
  return {
    message: get(state, 'userInterface.message', {})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    closeMessage: bindActionCreators(closeMessageEvent, dispatch)
  }
}

Message.propTypes = {
  message: PropTypes.object.isRequired,
  closeMessage: PropTypes.func.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(Message)
