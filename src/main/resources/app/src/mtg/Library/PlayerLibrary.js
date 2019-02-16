import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card'
import {LibraryUiUtils} from './LibraryUiUtils'

class PlayerLibrary extends PureComponent {
  render() {
    return (
      <div id="player-library">
        {this.props.cards.length > 0 ? <Card name='card' style={LibraryUiUtils.cardHeight(this.props.cards.length)} /> : null}
        <div className='card-bottom-thickness' style={LibraryUiUtils.cardBottomThickness(this.props.cards.length)} />
        <div className='card-right-thickness' style={LibraryUiUtils.cardRightThickness(this.props.cards.length)} />
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    cards: get(state, 'player.library.cards', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerLibrary)