import React, {Component} from 'react'
import get from 'lodash/get'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import PropTypes from 'prop-types'
import './userAction.scss'

class UserAction extends Component {
  constructor(props) {
    super(props)
    this.handleContinueKey = this.handleContinueKey.bind(this)
  }

  handleContinueKey(event) {
    console.log('isPopupOpen: ', this.props.isPopupOpen)
    if (!this.props.isPopupOpen) {
      if (event.key === ' ') {
        if (this.isContinueEnabled()) {
          this.props.continueClick()
        }
      } else if (event.key === 'H') {
        this.props.openHelpPage(true)
      }
    }
  }

  componentDidMount() {
    document.addEventListener('keydown', this.handleContinueKey)
  }

  componentWillUnmount() {
    document.removeEventListener('keydown', this.handleContinueKey)
  }

  isPhaseActiveForPlayer() {
    return this.props.turn.currentPhaseActivePlayer === this.props.currentPlayerName
  }

  isContinueEnabled() {
    return !this.props.winner && this.isPhaseActiveForPlayer()
  }

  render() {
    return (
      <div id='user-actions'>
        <button title="Press SPACE to continue" id='continue-button' type='button' disabled={!this.isContinueEnabled()} onClick={this.props.continueClick}>-></button>
        <button title="Press L to see game logs" id='logs-button' type='button' onClick={() => alert('Game Log coming soon!')}>=</button>
        <button title="Press H to see the help page" id='help-button' type='button' onClick={this.props.openHelpPage}>?</button>
      </div>

    )
  }
}

const createContinueClickAction = () => {
  return {
    type: 'CONTINUE_CLICK'
  }
}

const openHelpPageAction = () => {
  return {
    type: 'OPEN_HELP_PAGE'
  }
}

const mapStateToProps = state => {
  return {
    turn: get(state, 'turn', {}),
    currentPlayerName: get(state, 'player.name', ''),
    winner: get(state, 'turn.winner'),
    isPopupOpen: get(state, 'userInterface.message.text', false)
  }
}

const mapDispatchToProps = dispatch => {
  return {
    continueClick: bindActionCreators(createContinueClickAction, dispatch),
    openHelpPage: bindActionCreators(openHelpPageAction, dispatch)
  }
}

UserAction.propTypes = {
  turn: PropTypes.object.isRequired,
  currentPlayerName: PropTypes.string.isRequired,
  winner: PropTypes.string,
  continueClick: PropTypes.func.isRequired,
  openHelpPage: PropTypes.func.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(UserAction)