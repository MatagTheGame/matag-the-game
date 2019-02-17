import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'

class Phase extends PureComponent {
  render() {
    let classes = this.props.status
    if (this.props.active) {
      classes += ' active'

      if (this.props.activeForPlayer) {
        classes += ' active-for-player'
      } else {
        classes += ' active-for-opponent'
      }
    }

    return <span key={this.props.name} className={classes}>{this.props.name}</span>
  }
}

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