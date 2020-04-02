import React from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import _ from 'lodash'
import './playerActiveMana.scss'

function PlayerActiveMana(props) {
  return (
    <div id='player-active-mana'>
      <ul>
        { _.flatten(_.values(props.mana)).sort().map((mana, index) => <li key={index}><img alt={mana} title={mana} src={`/img/symbols/${mana}.png`} /></li>) }
      </ul>
    </div>
  )
}

const mapStateToProps = state => {
  return {
    mana: get(state, 'player.mana', {})
  }
}

PlayerActiveMana.propTypes = {
  type: PropTypes.string.isRequired,
  mana: PropTypes.object.isRequired
}

export default connect(mapStateToProps)(PlayerActiveMana)