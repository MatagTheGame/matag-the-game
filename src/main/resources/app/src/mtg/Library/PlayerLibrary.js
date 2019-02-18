import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import Card from '../Card/Card'
import {LibraryUiUtils} from './LibraryUiUtils'

class PlayerLibrary extends PureComponent {
  render() {
    return (
      <div id="player-library" className='player-library'>
        {this.props.cards.length > 0 ? <Card name='card' style={LibraryUiUtils.libraryHeight(this.props.cards.length, 'player')} /> : null}
        <div className='card-bottom-thickness' style={LibraryUiUtils.libraryBottomThickness(this.props.cards.length)} />
        <div className='card-right-thickness' style={LibraryUiUtils.libraryRightThickness(this.props.cards.length)} />
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    cards: get(state, 'player.library', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(PlayerLibrary)