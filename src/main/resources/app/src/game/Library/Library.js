import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import {LibraryUiUtils} from './LibraryUiUtils'
import Card from '../Card/Card'

class Library extends PureComponent {
  getId() {
    return this.props.type + '-library'
  }
  
  getLibrarySize() {
    if (this.props.type === 'player') {
      return this.props.playerLibrarySize
    } else {
      return this.props.opponentLibrarySize
    }
  }
  
  render() {
    return (
      <div id={this.getId()} className='player-library'>
        {this.getLibrarySize() > 0 ? <Card style={LibraryUiUtils.libraryHeight(this.getLibrarySize(), this.props.type)} /> : null}
        <div className='card-bottom-thickness' style={LibraryUiUtils.libraryBottomThickness(this.getLibrarySize())} />
        <div className='card-right-thickness' style={LibraryUiUtils.libraryRightThickness(this.getLibrarySize())} />
        {this.getLibrarySize() > 0 ? <span className='library-size'>{ this.getLibrarySize() }</span> : null}
      </div>
    )
  }
}

const mapStateToProps = state => {
  return {
    playerLibrarySize: get(state, 'player.librarySize', 0),
    opponentLibrarySize: get(state, 'opponent.librarySize', 0)
  }
}

export default connect(mapStateToProps)(Library)