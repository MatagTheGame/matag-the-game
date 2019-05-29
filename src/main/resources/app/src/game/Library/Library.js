import React, {Component} from 'react';
import {connect} from 'react-redux'
import {get} from 'lodash'
import {LibraryUiUtils} from './LibraryUiUtils'
import Card from '../Card/Card'
import PropTypes from 'prop-types'

class Library extends Component {
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
        {this.getLibrarySize() > 0 ? <Card style={LibraryUiUtils.libraryHeight(this.getLibrarySize(), this.props.type)} area='library' /> : null}
        <div className='card-bottom-thickness' style={LibraryUiUtils.libraryBottomThickness(this.getLibrarySize())} />
        <div className='card-right-thickness' style={LibraryUiUtils.libraryRightThickness(this.getLibrarySize())} />
        {this.getLibrarySize() > 0 ? <span className='library-size' style={LibraryUiUtils.libraryHeight(this.getLibrarySize(), this.props.type)}>{ this.getLibrarySize() }</span> : null}
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

Library.propTypes = {
  type: PropTypes.string.isRequired,
  playerLibrarySize: PropTypes.number.isRequired,
  opponentLibrarySize: PropTypes.number.isRequired,
}

export default connect(mapStateToProps)(Library)