import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'

class OpponentGraveyard extends PureComponent {
  render() {
    return (
      <div id="opponent-graveyard" className='graveyard'>
        {this.props.cards.map((cardInstance) => <CardComponent key={cardInstance.id} name={cardInstance.card.name} />)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    cards: get(state, 'opponent.graveyard', [])
  }
}

export default connect(mapStateToProps)(OpponentGraveyard)