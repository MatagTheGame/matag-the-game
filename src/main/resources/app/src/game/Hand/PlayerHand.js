import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'

class PlayerHand extends PureComponent {
  render() {
    return (
      <div id="player-hand" className='hand'>
        {this.props.cards.map((cardInstance) =>
          <CardComponent key={cardInstance.id} id={cardInstance.id} name={cardInstance.card.name} onclick={this.props.playerCardClick} />)}
      </div>
    )
  }
}

const createPlayerCardClickAction = (event) => {
  return {
    type: 'PLAYER_CARD_CLICK',
    cardId: event.target.id
  }
}

const mapStateToProps = state => {
  return {
    cards: get(state, 'player.hand', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playerCardClick: bindActionCreators(createPlayerCardClickAction, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerHand)