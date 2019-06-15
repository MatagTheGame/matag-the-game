import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {get} from 'lodash'
import PropTypes from 'prop-types'

class PlayerInfo extends Component {
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

  getLifeClasses() {
    if (this.getLife() <= 0) {
      return 'non-positive'
    }
  }

  getPlayerClasses() {
    let classes = 'player-info'

    if (this.props.currentTurnPlayer === this.getName()) {
      classes += ' active-player'
    } else {
      classes += ' inactive-player'
    }

    if (this.props.winner === this.getName()) {
      classes += ' winner'
    }

    return classes
  }

  playerClick(playerName) {
    this.props.playerClick(playerName)
  }

  render() {
    return (
      <div id={this.getId()} className={this.getPlayerClasses()} onClick={() => this.playerClick(this.getName())}>
        <span>{this.getName()}</span>
        <span className={this.getLifeClasses()}>{this.getLife()}</span>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    playerName: get(state, 'player.name', ''),
    playerLife: get(state, 'player.life', 0),
    opponentName: get(state, 'opponent.name', ''),
    opponentLife: get(state, 'opponent.life', 0),
    currentTurnPlayer: get(state, 'turn.currentTurnPlayer'),
    winner: get(state, 'turn.winner')
  }
}

const createPlayerClickAction = (playerName) => {
  return {
    type: 'PLAYER_CLICK',
    playerName: playerName
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playerClick: bindActionCreators(createPlayerClickAction, dispatch)
  }
}

PlayerInfo.propTypes = {
  type: PropTypes.string.isRequired,
  playerName: PropTypes.string.isRequired,
  playerLife: PropTypes.number.isRequired,
  opponentName: PropTypes.string.isRequired,
  opponentLife: PropTypes.number.isRequired,
  currentTurnPlayer: PropTypes.string,
  winner: PropTypes.string
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerInfo)