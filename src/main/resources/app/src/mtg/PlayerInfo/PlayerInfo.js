import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'

class PlayerInfo extends PureComponent {
  render() {
    return (
      <div id="player-info" className='player-info'>
        <span>{this.props.name}</span>
        <span>{this.props.life}</span>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    name: get(state, 'player.name', ''),
    life: get(state, 'player.life', '')
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerInfo)