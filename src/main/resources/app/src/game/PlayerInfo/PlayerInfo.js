import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'

class PlayerInfo extends PureComponent {
  render() {
    let classes = 'player-info'

    if (this.props.currentTurnPlayer === this.props.name) {
      classes += ' active-player'
    } else {
      classes += ' inactive-player'
    }

    return (
      <div id="player-info" className={classes}>
        <span>{this.props.name}</span>
        <span>{this.props.life}</span>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    name: get(state, 'player.name', ''),
    life: get(state, 'player.life', ''),
    currentTurnPlayer: get(state, 'turn.currentTurnPlayer')
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerInfo)