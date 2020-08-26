import React, {Component} from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import {LibraryUiUtils} from './LibraryUiUtils'
import Card from 'game/Card/Card'
import './library.scss'

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

  getVisibleLibrary() {
    if (this.props.type === 'player') {
      return this.props.visibleLibrary
    } else {
      return []
    }
  }
  
  render() {
    return (
      <div id={this.getId()} className='player-library'>
        {this.getVisibleLibrary().length > 0 ? <div className='visible-cards'>
          {this.getVisibleLibrary().map((cardInstance) => <Card key={cardInstance.id} cardInstance={cardInstance} area='library' />)}) }
        </div> : null}

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
    opponentLibrarySize: get(state, 'opponent.librarySize', 0),
    visibleLibrary: get(state, 'player.visibleLibrary', [])
  }
}

Library.propTypes = {
  type: PropTypes.string.isRequired,
  playerLibrarySize: PropTypes.number.isRequired,
  opponentLibrarySize: PropTypes.number.isRequired,
  visibleLibrary: PropTypes.array.isRequired,
}

export default connect(mapStateToProps)(Library)