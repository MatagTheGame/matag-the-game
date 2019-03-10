import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import {bindActionCreators} from 'redux'
import CardSearch from '../Card/CardSearch'

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
    return CardSearch.cards(this.getBattlefield()).ofType('CREATURE')
  }

  playerCardClick() {
    if (this.props.type === 'player') {
      return this.props.playerCardClick
    } else {
      return () => {}
    }
  }

  cardItems(cards) {
    return cards.map((cardInstance) =>
      <Card key={cardInstance.id} cardInstance={cardInstance} onclick={this.playerCardClick()} />)
  }

  render() {
    return (
      <div id={this.getId()} className='battlefield'>
        <div className='battlefield-area'>{this.cardItems(this.getSecondLineCards())}</div>
        <div className='battlefield-area'>{this.cardItems(this.getFirstLineCards())}</div>
      </div>
    )
  }
}

const createBattlefieldPlayerCardClickAction = (event) => {
  return {
    type: 'PLAYER_BATTLEFIELD_CARD_CLICK',
    cardId: event.target.id
  }
}

const mapStateToProps = state => {
  return {
    playerBattlefield: get(state, 'player.battlefield', []),
    opponentBattlefield: get(state, 'opponent.battlefield', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playerCardClick: bindActionCreators(createBattlefieldPlayerCardClickAction, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Battlefield)