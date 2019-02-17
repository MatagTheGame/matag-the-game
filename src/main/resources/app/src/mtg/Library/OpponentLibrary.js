import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import Card from '../Card'
import {get} from 'lodash';

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
    cards: get(state, 'opponent.library', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(OpponentLibrary)