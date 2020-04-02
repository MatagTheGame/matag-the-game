import React from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import Card from '../Card/Card'
import PropTypes from 'prop-types'
import './hand.scss'

function Hand(props) {
  const getId = () => {
    return props.type + '-hand'
  }

  const getHand = () => {
    if (props.type === 'player') {
      return props.playerHand
    } else {
      return props.opponentHand
    }
  }

  const playerCardClick = (cardId) => {
    if (props.type === 'player') {
      return (e) => props.playerCardClick(cardId, {x: e.screenX, y: e.screenY})
    } else {
      return () => {}
    }
  }

  const isCardSelectedToBePlayed = (cardInstance) => {
    return cardInstance.id === props.cardIdSelectedToBePlayed
  }

  return (
    <div id={getId()} className='hand'>
      {getHand().map((cardInstance) =>
        <Card key={cardInstance.id} cardInstance={cardInstance} onclick={playerCardClick(cardInstance.id)}
          selected={isCardSelectedToBePlayed(cardInstance)} area='hand' />)}
    </div>
  )
}

const createHandPlayerCardClickAction = (cardId, event) => {
  return {
    type: 'PLAYER_HAND_CARD_CLICK',
    cardId: cardId,
    event: event
  }
}

const mapStateToProps = state => {
  return {
    playerHand: get(state, 'player.hand', []),
    opponentHand: get(state, 'opponent.hand', []),
    cardIdSelectedToBePlayed: get(state, 'turn.cardIdSelectedToBePlayed')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playerCardClick: bindActionCreators(createHandPlayerCardClickAction, dispatch)
  }
}

Hand.propTypes = {
  type: PropTypes.string.isRequired,
  playerHand: PropTypes.array.isRequired,
  opponentHand: PropTypes.array.isRequired,
  cardIdSelectedToBePlayed: PropTypes.number,
  playerCardClick: PropTypes.func.isRequired
}

export default connect(mapStateToProps, mapDispatchToProps)(Hand)