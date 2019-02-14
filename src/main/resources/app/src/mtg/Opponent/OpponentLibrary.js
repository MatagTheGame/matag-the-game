import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import Card from '../Card'

class OpponentLibrary extends PureComponent {
  render() {
    return (
      <div id="opponent-library">
        {this.props.cards.length > 0 ? <Card name={'card'} /> : null}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    cards: state.opponent.library.cards
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(OpponentLibrary)