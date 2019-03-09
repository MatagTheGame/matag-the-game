import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'

class PlayerHand extends PureComponent {
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

  getPlayerCardClick () {
    if (this.props.type === 'player') {
      return this.props.playerCardClick
    } else {
      return () => {}
    }
  }

  render() {
    return (
      <div id={this.getId()} className='hand'>
        {this.getHand().map((cardInstance) =>
          <CardComponent key={cardInstance.id} id={cardInstance.id} name={cardInstance.card.name} onclick={this.getPlayerCardClick()} />)}
      </div>
    )
  }
}

const createHandPlayerCardClickAction = (event) => {
  return {
    type: 'PLAYER_HAND_CARD_CLICK',
    cardId: event.target.id
  }
}

const mapStateToProps = state => {
  return {
    playerHand: get(state, 'player.hand', []),
    opponentHand: get(state, 'opponent.hand', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playerCardClick: bindActionCreators(createHandPlayerCardClickAction, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerHand)