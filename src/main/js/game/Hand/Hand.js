import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import Card from 'game/Card/Card'
import './hand.scss'

class Hand extends Component {
  getId() {
    return this.props.type + '-hand'
  }

  getHand() {
    if (this.props.type === 'player') {
      return this.props.playerHand
    } else {
      return this.props.opponentHand
    }
  }

  playerCardClick(cardId) {
    if (this.props.type === 'player') {
      return (e) => this.props.playerCardClick(cardId, {x: e.screenX, y: e.screenY})
    } else {
      return () => {}
    }
  }

  isCardSelectedToBePlayed(cardInstance) {
    return cardInstance.id === this.props.cardIdSelectedToBePlayed
  }

  isCardTargeted(cardInstance) {
    console.log('this.props.targetIds: ', this.props.targetIds)
    return this.props.targetIds.indexOf(cardInstance.id) >= 0
  }

  render() {
    return (
      <div id={this.getId()} className='hand'>
        {this.getHand().map((cardInstance) =>
          <Card key={cardInstance.id} cardInstance={cardInstance} onclick={this.playerCardClick(cardInstance.id)}
            selected={this.isCardSelectedToBePlayed(cardInstance) || this.isCardTargeted(cardInstance)} area='hand' />)}
      </div>
    )
  }
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
    cardIdSelectedToBePlayed: get(state, 'turn.cardIdSelectedToBePlayed'),
    targetIds: get(state, 'turn.targetsIds', [])
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
  playerCardClick: PropTypes.func.isRequired,
  targetIds: PropTypes.array.isRequired,
}

export default connect(mapStateToProps, mapDispatchToProps)(Hand)