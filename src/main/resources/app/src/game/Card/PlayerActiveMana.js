import React, {Component} from 'react'
import {connect} from 'react-redux'
import {get} from 'lodash'
import PropTypes from 'prop-types'

class PlayerActiveMana extends Component {
  render() {
    return (
      <div id='player-active-mana'>
        <ul>
          { Object.values(this.props.mana).map((mana, index) => <li key={index}><img alt={mana} title={mana} src={`/img/symbols/${mana}.png`} /></li>) }
        </ul>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    playerName: get(state, 'player.name', ''),
    mana: get(state, 'player.mana', {})
  }
}

PlayerActiveMana.propTypes = {
  type: PropTypes.string.isRequired,
  mana: PropTypes.object.isRequired
}

export default connect(mapStateToProps)(PlayerActiveMana)