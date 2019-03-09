import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'

class PlayerInfo extends PureComponent {
  getId () {
    return this.props.type + '-info'
  }

  getName() {
    if (this.props.type === 'player') {
      return this.props.playerName
    } else {
      return this.props.opponentName
    }
  }

  getLife() {
    if (this.props.type === 'player') {
      return this.props.playerLife
    } else {
      return this.props.opponentLife
    }
  }

  render() {
    let classes = 'player-info'

    if (this.props.currentTurnPlayer === this.getName()) {
      classes += ' active-player'
    } else {
      classes += ' inactive-player'
    }

    return (
      <div id={this.getId()} className={classes}>
        <span>{this.getName()}</span>
        <span>{this.getLife()}</span>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    playerName: get(state, 'player.name', ''),
    playerLife: get(state, 'player.life', ''),
    opponentName: get(state, 'opponent.name', ''),
    opponentLife: get(state, 'opponent.life', ''),
    currentTurnPlayer: get(state, 'turn.currentTurnPlayer')
  }
}

export default connect(mapStateToProps)(PlayerInfo)