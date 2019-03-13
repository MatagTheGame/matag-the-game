import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import {bindActionCreators} from 'redux'

class UserAction extends PureComponent {
  constructor(props) {
    super(props);
    this.handleContinueKey = this.handleContinueKey.bind(this);
  }

  handleContinueKey(event) {
    if (event.key === ' ') {
      if (!this.props.winner && this.isPhaseActiveForPlayer()) {
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

  render() {
    return (
      <div id='user-actions'>
        <button title="Press SPACE to continue" id='continue-button' type='button' disabled={!this.isPhaseActiveForPlayer()} onClick={this.props.continueClick}>-></button>
        <button title="Press L to see game logs" id='logs-button' type='button'>=</button>
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

export default connect(mapStateToProps, mapDispatchToProps)(UserAction)