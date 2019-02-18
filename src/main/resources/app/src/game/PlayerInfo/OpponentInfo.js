import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'

class OpponentInfo extends PureComponent {
  render() {
    let classes = 'player-info'

    if (this.props.currentTurnPlayer === this.props.name) {
      classes += ' active-player'
    } else {
      classes += ' inactive-player'
    }

    return (
      <div id="opponent-info" className={classes}>
        <span>{this.props.name}</span>
        <span>{this.props.life}</span>
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    name: get(state, 'opponent.name', ''),
    life: get(state, 'opponent.life', ''),
    currentTurnPlayer: get(state, 'turn.status.currentTurnPlayer')
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(OpponentInfo)