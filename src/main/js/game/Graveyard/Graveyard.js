import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import Card from 'game/Card/Card'
import './graveyard.scss'

class Graveyard extends Component {
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
      <div id={this.id} className='graveyard'>
        {this.graveyard.map((cardInstance) =>
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

Graveyard.propTypes = {
  type: PropTypes.string.isRequired,
  playerGraveyard: PropTypes.array.isRequired,
  opponentGraveyard: PropTypes.array.isRequired
}

export default connect(mapStateToProps)(Graveyard)