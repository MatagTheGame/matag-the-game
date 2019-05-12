import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'

class Graveyard extends PureComponent {
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
          <Card key={cardInstance.id} cardInstance={cardInstance} area='graveyard' />)}
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

export default connect(mapStateToProps)(Graveyard)