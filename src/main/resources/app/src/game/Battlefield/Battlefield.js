import React, {PureComponent} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import {bindActionCreators} from 'redux'

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

  getLands() {
    return this.getBattlefield()
      .filter(cardInstance => cardInstance.card.types.includes('LAND'))
  }

  getCreatures() {
    return this.getBattlefield()
      .filter(cardInstance => cardInstance.card.types.includes('CREATURE'))
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
        <div className='battlefield-area'>{this.cardItems(this.getCreatures())}</div>
        <div className='battlefield-area'>{this.cardItems(this.getLands())}</div>
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