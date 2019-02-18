import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Phase from './Phase'

class TurnPhases extends PureComponent {
  isPhaseActiveForPlayer() {
    return this.props.turnStatus.currentPhaseActivePlayer === this.props.currentPlayerName
  }

  isPhaseActive(name) {
    return this.props.turnStatus.currentPhase === name
  }

  render() {
    return (
      <div id='turn-phases'>
        {this.props.phases.map((phase) =>
          <Phase key={phase.name}
                 name={phase.name}
                 status={phase.status.toLowerCase()}
                 active={this.isPhaseActive(phase.name)}
                 activeForPlayer={this.isPhaseActiveForPlayer()}/>
                 )}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    phases: get(state, 'turn.phasesConfig', []),
    turnStatus: get(state, 'turn.status', {}),
    currentPlayerName: get(state, 'player.name', '')
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(TurnPhases)