import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'

class OpponentLandArea extends PureComponent {
  render() {
    return (
      <div id="opponent-land-area" className='land-area'>
        {this.props.cards.map((cardInstance) => <CardComponent key={cardInstance.id} name={cardInstance.card.name} />)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    cards: get(state, 'opponent.battlefield', [])
  }
}

export default connect(mapStateToProps)(OpponentLandArea)