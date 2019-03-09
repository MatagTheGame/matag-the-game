import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import CardComponent from '../Card/CardComponent'

class PlayerGraveyard extends PureComponent {
  getId() {
    return this.props.type + '-graveyard'
  }

  getGraveyard() {
    if (this.props.type === 'player') {
      return this.props.playerGraveyard
    } else {
      return this.props.opponentGraveyard
    }
  }

  render() {
    return (
      <div id={this.getId()} className='graveyard'>
        {this.getGraveyard().map((cardInstance) =>
          <CardComponent key={cardInstance.id} id={cardInstance.id} name={cardInstance.card.name} />)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    playerGraveyard: get(state, 'player.graveyard', []),
    opponentGraveyard: get(state, 'opponent.graveyard', [])
  }
}

export default connect(mapStateToProps)(PlayerGraveyard)