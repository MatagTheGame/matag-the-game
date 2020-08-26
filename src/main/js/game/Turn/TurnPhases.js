import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import Phase from './Phase'
import PropTypes from 'prop-types'
import './turhPhases.scss'
import TurnUtils from 'game/Turn/TurnUtils'

class TurnPhases extends Component {
  isPhaseActiveForPlayer() {
    return this.props.turn.currentPhaseActivePlayer === this.props.currentPlayerName
  }

  isPhaseActive(name) {
    return this.props.turn.currentPhase === name
  }

  render() {
    const phases = TurnUtils.phasesToRender(this.props.turn.currentPhase)
    return (
      <div id='turn-phases'>
        {phases.map((phase, index) =>
          <Phase key={phase === '...' ? phase + index : phase}
            name={phase}
            title={Phase.getPhaseName(phase)}
            active={this.isPhaseActive(phase)}
            activeForPlayer={this.isPhaseActiveForPlayer()}/>
        )}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    turn: get(state, 'turn', {}),
    currentPlayerName: get(state, 'player.name', '')
  }
}

TurnPhases.propTypes = {
  turn: PropTypes.object.isRequired,
  currentPlayerName: PropTypes.string.isRequired
}

export default connect(mapStateToProps)(TurnPhases)