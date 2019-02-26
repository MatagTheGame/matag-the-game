import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import PlayerUtils from '../PlayerInfo/PlayerUtils'

class StatusMessageComponent extends PureComponent {
  static getDefaultStandbyMessageForUser (state) {
    if (PlayerUtils.isCurrentPlayerActive(state)) {
      return 'Play any spell or abilities or continue (SPACE).'
    } else {
      return 'Wait for opponent to perform its action...'
    }
  }

  render() {
    return (
      <div id='status-message'>
        {this.props.statusMessage}
      </div>
    )
  }
}


const mapStateToProps = state => {
  return {
    statusMessage: state.statusMessage
  }
}

export default connect(mapStateToProps)(StatusMessageComponent)