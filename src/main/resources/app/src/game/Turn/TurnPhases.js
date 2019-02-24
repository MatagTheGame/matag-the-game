import React, {Fragment, PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Phase from './Phase'
import {bindActionCreators} from 'redux'

class TurnPhases extends PureComponent {
  constructor(props) {
    super(props);
    this.handleContinueKey = this.handleContinueKey.bind(this);
  }

  handleContinueKey(event) {
    if (event.key === ' ') {
      this.props.continueClick()
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

  isPhaseActive(name) {
    return this.props.turn.currentPhase === name
  }

  renderContinueButton() {
    if (this.isPhaseActiveForPlayer()) {
      return <button title="Press SPACE to continue" id='continue-button' type='button' onClick={this.props.continueClick}>Continue</button>
    }
  }

  render() {
    return (
      <Fragment>
        <div id='turn-phases'>
          {Phase.getPhases().map((phase) =>
            <Phase key={phase}
                   name={phase}
                   active={this.isPhaseActive(phase)}
                   activeForPlayer={this.isPhaseActiveForPlayer()}/>
                   )}
        </div>
        { this.renderContinueButton() }
      </Fragment>

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
    currentPlayerName: get(state, 'player.name', '')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    continueClick: bindActionCreators(createContinueClickAction, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(TurnPhases)