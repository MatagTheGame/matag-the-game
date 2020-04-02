import React from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import './playerInfo.scss'

function PlayerInfo(props) {
  const getId = () => {
    return props.type + '-info'
  }

  const getName = () => {
    if (props.type === 'player') {
      return props.playerName
    } else {
      return props.opponentName
    }
  }

  const getLife = () => {
    if (props.type === 'player') {
      return props.playerLife
    } else {
      return props.opponentLife
    }
  }

  const getLifeClasses = () => {
    if (getLife() <= 0) {
      return 'non-positive'
    }
  }

  const getPlayerClasses = () => {
    let classes = 'player-info'

    if (props.currentTurnPlayer === getName()) {
      classes += ' active-player'
    } else {
      classes += ' inactive-player'
    }

    if (props.winner === getName()) {
      classes += ' winner'
    }

    return classes
  }

  const playerClick = (playerName) => {
    props.playerClick(playerName)
  }

  return (
    <div id={getId()} className={getPlayerClasses()} onClick={() => playerClick(getName())}>
      <span>{getName()}</span>
      <span className={getLifeClasses()}>{getLife()}</span>
    </div>
  )
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