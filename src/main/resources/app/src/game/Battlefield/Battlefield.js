import React, {Component, Fragment} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import {bindActionCreators} from 'redux'
import CardSearch from '../Card/CardSearch'
import CardUtils from '../Card/CardUtils'
import PlayerUtils from '../PlayerInfo/PlayerUtils'
import PropTypes from 'prop-types'

class Battlefield extends Component {
  constructor(props) {
    super(props)
    this.cardWithAttachments = this.cardWithAttachments.bind(this)
  }

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
      .sort((l, r) => l.card.name.localeCompare(r.card.name))
      .map(this.cardWithAttachments)
  }

  getSecondLineCards() {
    return CardSearch.cards(this.getPlayerBattlefield()).ofType('CREATURE').notAttackingOrBlocking()
      .concat(CardSearch.cards(this.getPlayerBattlefield()).ofAnyType(['ARTIFACT', 'ENCHANTMENT']).notAttached())
      .map(this.cardWithAttachments)
  }

  getAttackingOrBlockingCreatures() {
    return CardSearch.cards(this.getPlayerBattlefield()).attackingOrBlocking()
      .map(this.cardWithAttachments)
  }

  isCardSelectedToBeBlocked(cardInstance, index) {
    return this.props.turn.currentPhase === 'DB' && PlayerUtils.isCurrentPlayerActive(this.props.state)
      && this.props.turn.blockingCardPosition === index && CardUtils.isAttacking(cardInstance)
  }

  cardGroups(cardGroups) {
    return cardGroups.map((cardGroup) => {
      return <span className='group' key={cardGroup[0].id}>
        {this.cardGroup(cardGroup)}
      </span>
    })
  }

  cardGroup(cardGroup, position=-1) {
    if (cardGroup.length === 1) {
      return this.singleCardInstance(cardGroup[0], position)

    } else {
      return this.cardGroupInstance(cardGroup, position)
    }
  }

  positionedCardGroups(cardGroups) {
    return cardGroups.map((cardGroup, i) =>
      <span key={cardGroup[0].id} style={{'marginLeft': this.cardMarginLeft(cardGroup[0]), 'marginTop': this.attackingCardMarginTop(cardGroup[0])}}>
        {this.cardGroup(cardGroup, i)}
      </span>
    )
  }

  singleCardInstance(cardInstance, i=-1) {
    return <Card key={cardInstance.id} cardInstance={cardInstance} onclick={this.playerCardClick(cardInstance.id)}
      selected={this.isCardSelectedToBeBlocked(cardInstance, i) || this.isCardSelectedToBePlayed(cardInstance)} area='battlefield'/>
  }

  cardGroupInstance(cardGroup, position=-1) {
    return cardGroup.slice().reverse().map((cardInstance, i) => {
      const marginLeft = -(cardGroup.length-i-1)*50
      const marginTop = -(cardGroup.length-i-1)*25
      return (
        <span key={cardInstance.id} style={{'marginLeft': `${marginLeft}px`, 'marginTop': `${marginTop}px`}}>
          {this.singleCardInstance(cardInstance, position)}
        </span>)
    })
  }

  attackingCards() {
    return CardSearch.cards(this.getPlayerBattlefield()).attackingOrFrontendAttacking().concat(CardSearch.cards(this.getOtherPlayerBattlefield()).attackingOrFrontendAttacking())
  }

  blockingCards() {
    return CardSearch.cards(this.getOtherPlayerBattlefield()).blockingOrFrontendBlocking().concat(CardSearch.cards(this.getPlayerBattlefield()).blockingOrFrontendBlocking())
  }

  static marginFromPosition(n, i) {
    const HALF_CARD = 130
    return ((-n + 1) + (2 * i)) * HALF_CARD
  }

  static getAttackingCardPosition(attackingCards, cardInstance) {
    if (CardUtils.isAttackingOrFrontendAttacking(cardInstance)) {
      return attackingCards.indexOfId(cardInstance.id)
    } else {
      return attackingCards.indexOfId(cardInstance.modifiers.blockingCardId)
    }
  }

  getBlockingCardPosition(cardInstance) {
    return this.blockingCards()
      .filter(currentCardInstance => cardInstance.modifiers.blockingCardId === currentCardInstance.modifiers.blockingCardId)
      .map(cardInstance => cardInstance.id)
      .indexOf(cardInstance.id)
  }

  cardMarginLeft(cardInstance) {
    const attackingCards = this.attackingCards()
    const numOfAttackingCreatures = attackingCards.length
    const cardPosition = Battlefield.getAttackingCardPosition(attackingCards, cardInstance)
    const marginFromPosition = Battlefield.marginFromPosition(numOfAttackingCreatures, cardPosition)
    let margin = CardUtils.isAttackingOrFrontendAttacking(cardInstance) ? marginFromPosition : -marginFromPosition
    margin += this.attackingCardMarginTop(cardInstance) / 2
    return margin
  }

  static cardMarginTop(position) {
    return position * 50
  }

  attackingCardMarginTop(cardInstance) {
    return Battlefield.cardMarginTop(this.getBlockingCardPosition(cardInstance))
  }

  cardWithAttachments(card) {
    const attachments = CardSearch.cards(this.getPlayerBattlefield())
      .concat(this.getOtherPlayerBattlefield())
      .attachedTo(card.id)
    return [card, ...attachments]
  }

  playerCardClick(cardId) {
    if (this.props.type === 'player') {
      return () => this.props.playerCardClick(cardId)
    } else {
      return () => this.props.opponentCardClick(cardId)
    }
  }

  isCardSelectedToBePlayed(cardInstance) {
    return cardInstance.id === this.props.cardIdSelectedToBePlayed
  }

  render() {
    if (!this.props.turn) {
      return <Fragment />
    }

    return (
      <div id={this.getId()} className='battlefield'>
        <div className='battlefield-area combat-line'>{this.positionedCardGroups(this.getAttackingOrBlockingCreatures())}</div>
        <div className='battlefield-area second-line'>{this.cardGroups(this.getSecondLineCards())}</div>
        <div className='battlefield-area first-line'>{this.cardGroups(this.getFirstLineCards())}</div>
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
    cardIdSelectedToBePlayed: get(state, 'turn.cardIdSelectedToBePlayed'),
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

Battlefield.propTypes = {
  type: PropTypes.string.isRequired,
  playerBattlefield: PropTypes.array.isRequired,
  opponentBattlefield: PropTypes.array.isRequired,
  turn: PropTypes.object,
  state: PropTypes.object.isRequired,
  playerCardClick: PropTypes.func.isRequired,
  opponentCardClick: PropTypes.func.isRequired,
}

export default connect(mapStateToProps, mapDispatchToProps)(Battlefield)