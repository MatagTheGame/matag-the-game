import React, {PureComponent} from 'react'

export default class Phase extends PureComponent {

  static isPhaseEnabled(phasesConfig, phaseName) {
    const phaseConfig = phasesConfig.find((phaseConfig) => phaseConfig.name === phaseName)
    if (phaseConfig) {
      return phaseConfig.status === 'DISABLED'
    }
    return false
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

    return <span key={this.props.name} className={classes}>{this.props.name}</span>
  }
}