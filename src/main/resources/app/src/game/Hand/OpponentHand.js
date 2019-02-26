import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'

class OpponentHand extends PureComponent {
  render() {
    return (
      <div id="opponent-hand" className='hand'>
        {this.props.cards.map((cardInstance) => <CardComponent key={cardInstance.id} name={cardInstance.card.name} />)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    cards: get(state, 'opponent.hand', [])
  }
}

export default connect(mapStateToProps)(OpponentHand)