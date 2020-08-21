import React, {Component} from 'react'

export default class Phase extends Component {

  static getPhases() {
    return ['UT', 'UP', 'DR', 'M1', 'BC', 'DA', 'DB', 'FS', 'CD', 'EC', 'M2', 'ET', 'CL']
  }

  static getPhasesNames() {
    return ['Untap', 'Upkeep', 'Draw', 'Main 1', 'Begin Combat', 'Declare Attackers', 'Declare Blockers', 'First Strike',
      'Combat Damage', 'End of Combat', 'Main 2', 'End of Turn', 'Cleanup']
  }

  static getPhaseName(phase) {
    const index = Phase.getPhases().indexOf(phase);
    return Phase.getPhasesNames()[index]
  }

  static isMainPhase(phase) {
    return phase === 'M1' || phase === 'M2'
  }

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

    return <span key={this.props.name} title={this.props.title} className={classes}>{this.props.name}</span>
  }
}