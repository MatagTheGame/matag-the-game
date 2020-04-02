import React from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import Phase from './Phase'
import PropTypes from 'prop-types'
import './turhPhases.scss'
import {PhaseUtils} from './PhaseUtils'

function TurnPhases(props) {
  const isPhaseActiveForPlayer = () => {
    return props.turn.currentPhaseActivePlayer === props.currentPlayerName
  }

  const isPhaseActive = (name) => {
    return props.turn.currentPhase === name
  }

  return (
    <div id='turn-phases'>
      {PhaseUtils.getPhases().map((phase) =>
        <Phase key={phase}
          name={phase}
          active={isPhaseActive(phase)}
          activeForPlayer={isPhaseActiveForPlayer()}/>
      )}
    </div>
  )
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