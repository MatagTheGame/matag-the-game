import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash';
import {LibraryUiUtils} from './LibraryUiUtils'
import CardComponent from '../Card/CardComponent'

class OpponentLibrary extends PureComponent {
  render() {
    return (
      <div id="opponent-library" className='player-library'>
        {this.props.cards.length > 0 ? <CardComponent name={'card'} style={LibraryUiUtils.libraryHeight(this.props.cards.length, 'opponent')} /> : null}
        <div className='card-bottom-thickness' style={LibraryUiUtils.libraryBottomThickness(this.props.cards.length)} />
        <div className='card-right-thickness' style={LibraryUiUtils.libraryRightThickness(this.props.cards.length)} />
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