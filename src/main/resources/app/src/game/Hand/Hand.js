import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {get} from 'lodash'
import Card from '../Card/Card'

class Hand extends PureComponent {
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
      return () => this.props.playerCardClick(cardId)
    } else {
      return () => {}
    }
  }

  isCardSelectedToBePlayed(cardInstance) {
    return cardInstance.id === this.props.cardIdSelectedToBePlayed
  }

  render() {
    return (
      <div id={this.getId()} className='hand'>
        {this.getHand().map((cardInstance) =>
          <Card key={cardInstance.id} cardInstance={cardInstance} onclick={this.playerCardClick(cardInstance.id)}
                selected={this.isCardSelectedToBePlayed(cardInstance)} area='hand' />)}
      </div>
    )
  }
}

const createHandPlayerCardClickAction = (cardId) => {
  return {
    type: 'PLAYER_HAND_CARD_CLICK',
    cardId: cardId
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

export default connect(mapStateToProps, mapDispatchToProps)(Hand)