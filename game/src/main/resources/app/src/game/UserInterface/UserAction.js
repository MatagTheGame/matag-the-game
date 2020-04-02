import React, {useEffect} from 'react'
import get from 'lodash/get'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import PropTypes from 'prop-types'
import './userAction.scss'

function UserAction(props) {
  const handleKeyDown = (event) => {
    if (event.key === ' ') {
      if (isContinueEnabled()) {
        props.continueClick()
      }

    } else if (event.key === 'H') {
      props.openHelpPage(true)

    } else if (event.key === 'L') {
      alert('Game Log coming soon!')
    }
  }

  useEffect(() => {
    // FIXME this gets called millions of time
    console.log('registered again')
    document.addEventListener('keydown', handleKeyDown)
    return () => {
      document.removeEventListener('keydown', handleKeyDown)
    }
  }, [])

  const isPhaseActiveForPlayer = () => {
    return props.turn.currentPhaseActivePlayer === props.currentPlayerName
  }

  const isContinueEnabled = () => {
    return !props.winner && isPhaseActiveForPlayer()
  }

  return (
    <div id='user-actions'>
      <button title="Press SPACE to continue" id='continue-button' type='button' disabled={!isContinueEnabled()} onClick={props.continueClick}>-&gt;</button>
      <button title="Press L to see game logs" id='logs-button' type='button' onClick={() => alert('Game Log coming soon!')}>=</button>
      <button title="Press H to see the help page" id='help-button' type='button' onClick={props.openHelpPage}>?</button>
    </div>
  )
}

const createContinueClickAction = () => {
  return {
    type: 'CONTINUE_CLICK'
  }
}

const openHelpPageAction = () => {
  return {
    type: 'OPEN_HELP_PAGE'
  }
}

const mapStateToProps = state => {
  return {
    turn: get(state, 'turn', {}),
    currentPlayerName: get(state, 'player.name', ''),
    winner: get(state, 'turn.winner')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    continueClick: bindActionCreators(createContinueClickAction, dispatch),
    openHelpPage: bindActionCreators(openHelpPageAction, dispatch)
  }
}

UserAction.propTypes = {
  turn: PropTypes.object.isRequired,
  currentPlayerName: PropTypes.string.isRequired,
  winner: PropTypes.string,
  continueClick: PropTypes.func.isRequired,
  openHelpPage: PropTypes.func.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(UserAction)