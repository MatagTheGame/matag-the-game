import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import {bindActionCreators} from 'redux'
import CardSearch from '../Card/CardSearch'
import CardUtils from '../Card/CardUtils'
import PlayerUtils from '../PlayerInfo/PlayerUtils'

class Battlefield extends PureComponent {
  getId() {
    return this.props.type + '-battlefield'
  }

  getBattlefield() {
    if (this.props.type === 'player') {
      return this.props.playerBattlefield
    } else {
      return this.props.opponentBattlefield
    }
  }

  getFirstLineCards() {
    return CardSearch.cards(this.getBattlefield()).ofType('LAND')
  }

  getSecondLineCards() {
    return CardSearch.cards(this.getBattlefield()).ofType('CREATURE').notAttackingOrBlocking().notFrontendBlocking()
  }

  getAttackingBlockingCreatures() {
    return CardSearch.cards(this.getBattlefield()).attackingOrBlocking().concat(CardSearch.cards(this.getBattlefield()).frontendBlocking())
  }

  playerCardClick(cardId) {
    if (this.props.type === 'player') {
      return () => this.props.playerCardClick(cardId)
    } else {
      return () => this.props.opponentCardClick(cardId)
    }
  }

  isCardSelectedToBeBlocked(cardInstance, index) {
    return this.props.turn.currentPhase === 'DB' && PlayerUtils.isCurrentPlayerActive(this.props.state)
      && this.props.turn.blockingCardPosition === index && CardUtils.isAttacking(cardInstance)
  }

  cardItems(cards) {
    return cards.map((cardInstance, i) =>
      <Card key={cardInstance.id} cardInstance={cardInstance} onclick={this.playerCardClick(cardInstance.id)}
            selectedToBeBlocked={this.isCardSelectedToBeBlocked(cardInstance, i)} />)
  }

  render() {
    return (
      <div id={this.getId()} className='battlefield'>
        <div className='battlefield-area combat-line'>{this.cardItems(this.getAttackingBlockingCreatures())}</div>
        <div className='battlefield-area second-line'>{this.cardItems(this.getSecondLineCards())}</div>
        <div className='battlefield-area first-line'>{this.cardItems(this.getFirstLineCards())}</div>
      </div>
    )
  }
}

const createBattlefieldPlayerCardClickAction = (cardId) => {
  return {
    type: 'PLAYER_BATTLEFIELD_CARD_CLICK',
    cardId: cardId
  }
}

const createBattlefieldOpponentCardClickAction = (cardId) => {
  return {
    type: 'OPPONENT_BATTLEFIELD_CARD_CLICK',
    cardId: cardId
  }
}

const mapStateToProps = state => {
  return {
    playerBattlefield: get(state, 'player.battlefield', []),
    opponentBattlefield: get(state, 'opponent.battlefield', []),
    turn: state.turn,
    state: state
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playerCardClick: bindActionCreators(createBattlefieldPlayerCardClickAction, dispatch),
    opponentCardClick: bindActionCreators(createBattlefieldOpponentCardClickAction, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Battlefield)