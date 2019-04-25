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

  getPlayerBattlefield() {
    if (this.props.type === 'player') {
      return this.props.playerBattlefield
    } else {
      return this.props.opponentBattlefield
    }
  }

  getOtherPlayerBattlefield() {
    if (this.props.type === 'opponent') {
      return this.props.playerBattlefield
    } else {
      return this.props.opponentBattlefield
    }
  }

  getFirstLineCards() {
    return CardSearch.cards(this.getPlayerBattlefield()).ofType('LAND')
  }

  getSecondLineCards() {
    return CardSearch.cards(this.getPlayerBattlefield()).ofType('CREATURE').notAttackingOrBlocking()
  }

  getAttackingOrBlockingCreatures() {
    return CardSearch.cards(this.getPlayerBattlefield()).attackingOrBlocking()
  }

  isCardSelectedToBeBlocked(cardInstance, index) {
    return this.props.turn.currentPhase === 'DB' && PlayerUtils.isCurrentPlayerActive(this.props.state)
      && this.props.turn.blockingCardPosition === index && CardUtils.isAttacking(cardInstance)
  }

  cardItems(cards) {
    return cards.map((cardInstance, i) =>
      <Card key={cardInstance.id} cardInstance={cardInstance} onclick={this.playerCardClick(cardInstance.id)}
            selected={this.isCardSelectedToBeBlocked(cardInstance, i)} />)
  }

  attackingCards() {
    return CardSearch.cards(this.getPlayerBattlefield()).attackingOrFrontendAttacking().concat(CardSearch.cards(this.getOtherPlayerBattlefield()).attackingOrFrontendAttacking())
  }

  blockingCards() {
    return CardSearch.cards(this.getOtherPlayerBattlefield()).blockingOrFrontenBlocking().concat(CardSearch.cards(this.getPlayerBattlefield()).blockingOrFrontenBlocking())
  }

  static marginFromPosition(n, i) {
    const HALF_CARD = 130
    return ((-n + 1) + (2 * i)) * HALF_CARD
  }

  static getCardPosition(attackingCards, cardInstance) {
    if (CardUtils.isAttackingOrFrontendAttacking(cardInstance)) {
      return attackingCards.indexOfId(cardInstance.id)
    } else {
      return attackingCards.indexOfId(cardInstance.modifiers.blockingCardId)
    }
  }

  cardMarginLeft(cardInstance) {
    const attackingCards = this.attackingCards()
    const numOfAttackingCreatures = attackingCards.length
    const cardPosition = Battlefield.getCardPosition(attackingCards, cardInstance)
    const marginFromPosition = Battlefield.marginFromPosition(numOfAttackingCreatures, cardPosition)
    let margin = CardUtils.isAttackingOrFrontendAttacking(cardInstance) ? marginFromPosition : -marginFromPosition
    margin += this.cardMarginTop(cardInstance) / 2
    return margin
  }

  cardMarginTop(cardInstance) {
    const position = this.blockingCards()
      .filter(currentCardInstance => cardInstance.modifiers.blockingCardId === currentCardInstance.modifiers.blockingCardId)
      .map(cardInstance => cardInstance.id)
      .indexOf(cardInstance.id)
    return position * 50
  }

  positionedCardItems(cards) {
    return cards.map((cardInstance, i) =>
      <span key={cardInstance.id} style={{'marginLeft': this.cardMarginLeft(cardInstance), 'marginTop': this.cardMarginTop(cardInstance)}}>
        <Card cardInstance={cardInstance} onclick={this.playerCardClick(cardInstance.id)}
              selected={this.isCardSelectedToBeBlocked(cardInstance, i)} />
      </span>
    )
  }

  playerCardClick(cardId) {
    if (this.props.type === 'player') {
      return () => this.props.playerCardClick(cardId)
    } else {
      return () => this.props.opponentCardClick(cardId)
    }
  }

  render() {
    return (
      <div id={this.getId()} className='battlefield'>
        <div className='battlefield-area combat-line'>{this.positionedCardItems(this.getAttackingOrBlockingCreatures())}</div>
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