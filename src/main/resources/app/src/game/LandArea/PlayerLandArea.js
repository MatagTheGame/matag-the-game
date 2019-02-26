import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'

class PlayerLandArea extends PureComponent {
  render() {
    return (
      <div id="player-land-area" className='land-area'>
        {this.props.cards.map((cardInstance) => <CardComponent key={cardInstance.id} name={cardInstance.card.name} />)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    cards: get(state, 'player.battlefield', [])
  }
}

export default connect(mapStateToProps)(PlayerLandArea)