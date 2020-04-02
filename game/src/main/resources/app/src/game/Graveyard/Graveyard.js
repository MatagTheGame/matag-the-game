import React from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import Card from '../Card/Card'
import PropTypes from 'prop-types'
import './graveyard.scss'

function Graveyard(props) {
  const getId = () => {
    return props.type + '-graveyard'
  }

  const getGraveyard = () => {
    if (props.type === 'player') {
      return props.playerGraveyard
    } else {
      return props.opponentGraveyard
    }
  }

  return (
    <div id={getId()} className='graveyard'>
      {getGraveyard().map((cardInstance) =>
        <Card key={cardInstance.id} cardInstance={cardInstance} area='graveyard' />)}
    </div>
  )
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