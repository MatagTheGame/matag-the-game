import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import Card from './Card'

class OpponentHand extends PureComponent {
  render() {
    return (
      <div id="player-hand">
        {this.props.hand.map((card, i) => <Card key={i} name={card} />)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    hand: state.player.hand
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(OpponentHand)