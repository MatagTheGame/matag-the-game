import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'

class OpponentInfo extends PureComponent {
  render() {
    return (
      <div id="opponent-info" className='player-info'>
        <span>{this.props.name}</span>
        <span>{this.props.life}</span>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    name: get(state, 'opponent.name', ''),
    life: get(state, 'opponent.life', '')
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(OpponentInfo)