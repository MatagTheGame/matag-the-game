import React, {Component} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import {bindActionCreators} from 'redux'
import PropTypes from 'prop-types'

class UserAction extends Component {
  constructor(props) {
    super(props);
    this.handleContinueKey = this.handleContinueKey.bind(this);
  }

  handleContinueKey(event) {
    if (event.key === ' ') {
      if (this.isContinueEnabled()) {
        this.props.continueClick()
      }
    }
  }

  componentDidMount() {
    document.addEventListener('keydown', this.handleContinueKey);
  }

  componentWillUnmount() {
    document.removeEventListener('keydown', this.handleContinueKey);
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
      </div>

    )
  }
}

const createContinueClickAction = () => {
  return {
    type: 'CONTINUE_CLICK'
  }
}


const mapStateToProps = state => {
  return {
    turn: get(state, 'turn', {}),
    currentPlayerName: get(state, 'player.name', ''),
    winner: get(state, 'turn.winner')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    continueClick: bindActionCreators(createContinueClickAction, dispatch)
  }
}

UserAction.propTypes = {
  turn: PropTypes.object.isRequired,
  currentPlayerName: PropTypes.string.isRequired,
  winner: PropTypes.string,
  continueClick: PropTypes.func.isRequired,
}

export default connect(mapStateToProps, mapDispatchToProps)(UserAction)