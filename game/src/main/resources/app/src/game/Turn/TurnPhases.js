import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import Phase from './Phase'
import PropTypes from 'prop-types'

class TurnPhases extends Component {
  isPhaseActiveForPlayer() {
    return this.props.turn.currentPhaseActivePlayer === this.props.currentPlayerName
  }

  isPhaseActive(name) {
    return this.props.turn.currentPhase === name
  }

  render() {
    return (
      <div id='turn-phases'>
        {Phase.getPhases().map((phase) =>
          <Phase key={phase}
            name={phase}
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