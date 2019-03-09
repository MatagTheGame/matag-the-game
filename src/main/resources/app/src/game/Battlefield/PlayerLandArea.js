import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'
import {bindActionCreators} from 'redux'

class PlayerLandArea extends PureComponent {
  getId () {
    return this.props.type + '-land-area'
  }

  getBattlefield () {
    if (this.props.type === 'player') {
      return this.props.playerBattlefield
    } else {
      return this.props.opponentBattlefield
    }
  }

  getPlayerClickAction () {
    if (this.props.type === 'player') {
      return this.props.playerCardClick
    } else {
      return () => {}
    }
  }

  render() {
    return (
      <div id={this.getId()} className='land-area'>
        {this.getBattlefield().map((cardInstance) =>
          <CardComponent key={cardInstance.id} id={cardInstance.id} name={cardInstance.card.name} tapped={cardInstance.modifiers.tapped} onclick={this.getPlayerClickAction()} />)}
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

export default connect(mapStateToProps, mapDispatchToProps)(PlayerLandArea)