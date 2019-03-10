import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import {LibraryUiUtils} from './LibraryUiUtils'
import Card from '../Card/Card'

class Library extends PureComponent {
  getId() {
    return this.props.type + '-library'
  }
  
  getLibrary() {
    if (this.props.type === 'player') {
      return this.props.playerLibrary
    } else {
      return this.props.opponentLibrary
    }
  }
  
  render() {
    return (
      <div id={this.getId()} className='player-library'>
        {this.getLibrary().length > 0 ? <Card style={LibraryUiUtils.libraryHeight(this.getLibrary().length, this.props.type)} /> : null}
        <div className='card-bottom-thickness' style={LibraryUiUtils.libraryBottomThickness(this.getLibrary().length)} />
        <div className='card-right-thickness' style={LibraryUiUtils.libraryRightThickness(this.getLibrary().length)} />
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    playerLibrary: get(state, 'player.library', []),
    opponentLibrary: get(state, 'opponent.library', [])
  }
}

export default connect(mapStateToProps)(Library)