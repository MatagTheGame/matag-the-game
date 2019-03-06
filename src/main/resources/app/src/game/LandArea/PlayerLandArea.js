import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'
import {bindActionCreators} from 'redux'

class PlayerLandArea extends PureComponent {
  render() {
    return (
      <div id="player-land-area" className='land-area'>
        {this.props.cards.map((cardInstance) =>
          <CardComponent key={cardInstance.id} id={cardInstance.id} name={cardInstance.card.name} tapped={cardInstance.modifiers.tapped} onclick={this.props.playerCardClick} />)}
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
    cards: get(state, 'player.battlefield', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playerCardClick: bindActionCreators(createBattlefieldPlayerCardClickAction, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerLandArea)