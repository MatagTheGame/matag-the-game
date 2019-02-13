import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import Card from './Card'

class OpponentHand extends PureComponent {
  render() {
    return (
      <div id="opponent-hand">
        {this.props.hand.map((card, i) => <Card key={i} name={card} />)}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    hand: state.opponent.hand
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(OpponentHand)