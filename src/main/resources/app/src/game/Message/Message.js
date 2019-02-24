import React, {Fragment, PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import {bindActionCreators} from 'redux'

class Message extends PureComponent {
  constructor(props) {
    super(props);
    this.handleEscape = this.handleEscape.bind(this);
  }

  handleEscape(event) {
    if (event.key === 'Escape' && this.props.message.closable) {
      this.props.closeMessage(closeMessageEvent())
    }
  }

  componentDidMount() {
    document.addEventListener('keydown', this.handleEscape);
  }

  componentWillUnmount() {
    document.removeEventListener('keydown', this.handleEscape);
  }

  renderCloseButton() {
    if (this.props.message.closable) {
      return <i id='message-close' className='fa fa-window-close'
                onClick={this.props.closeMessage}
                aria-hidden='true'/>
    }
  }

  render() {
    if (this.props.message.text) {
      return (
        <div id='message'>
          { this.renderCloseButton() }
          { this.props.message.text }
        </div>
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
    message: get(state, 'message', {})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    closeMessage: bindActionCreators(closeMessageEvent, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Message)
