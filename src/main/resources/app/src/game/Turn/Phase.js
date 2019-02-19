import React, {PureComponent} from 'react'

export default class Phase extends PureComponent {

  static getPhases() {
    return ['UP', 'DR', 'M1', 'BC', 'DA', 'DB', 'FS', 'CD', 'EC', 'M2', 'ET', 'CL']
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