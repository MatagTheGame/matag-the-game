import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'

class Phase extends PureComponent {
  render() {
    let classes = this.props.status
    if (this.props.active) {
      classes += ' active'
    }

    return <span key={this.props.name} className={classes}>{this.props.name}</span>
  }
}

class TurnPhases extends PureComponent {
  isPhaseActiveForCurrentPlayer() {
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
                 active={this.isPhaseActive(phase.name)}/>)}
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