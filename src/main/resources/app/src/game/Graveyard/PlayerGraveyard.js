import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'

class PlayerGraveyard extends PureComponent {
  render() {
    return (
      <div id="player-graveyard" className='graveyard'>
        {this.props.cards.map((cardInstance) =>
          <CardComponent key={cardInstance.id} id={cardInstance.id} name={cardInstance.card.name} />)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    cards: get(state, 'player.graveyard', [])
  }
}

export default connect(mapStateToProps)(PlayerGraveyard)