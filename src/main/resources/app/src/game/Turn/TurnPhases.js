import React, {Fragment, PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Phase from './Phase'
import {bindActionCreators} from 'redux'

class TurnPhases extends PureComponent {
  isPhaseActiveForPlayer() {
    return this.props.turn.currentPhaseActivePlayer === this.props.currentPlayerName
  }

  isPhaseActive(name) {
    return this.props.turn.currentPhase === name
  }

  renderContinueButton() {
    if (this.isPhaseActiveForPlayer()) {
      return <button id='continue-button' type='button' onClick={this.props.continueClick}>Continue</button>
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

const createContinueClickAction = (event) => {
  return {
    type: 'CONTINUE_CLICK',
    cardId: event.target.id
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