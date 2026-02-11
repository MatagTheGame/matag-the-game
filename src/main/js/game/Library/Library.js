import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
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

  visibleCardClick(cardId) {
    if (this.props.type === 'player') {
      return (e) => this.props.visibleCardClick(cardId, {x: e.screenX, y: e.screenY})
    } else {
      return () => {}
    }
  }

  displayVisibleCards() {
    return (
      <div className='visible-cards'>
        {this.getVisibleLibrary().map((cardInstance) => <Card key={cardInstance.id} cardInstance={cardInstance}
                                  onclick={this.visibleCardClick(cardInstance.id)} area='visible-library' />)}
      </div>
    )
  }

  render() {
    return (
      <div id={this.id} className='player-library'>
        {this.getVisibleLibrary().length > 0 ? this.displayVisibleCards() : null}
        {this.getLibrarySize() > 0 ? <Card style={LibraryUiUtils.libraryHeight(this.getLibrarySize(), this.props.type)} area='library' /> : null}
        <div className='card-bottom-thickness' style={LibraryUiUtils.libraryBottomThickness(this.getLibrarySize())} />
        <div className='card-right-thickness' style={LibraryUiUtils.libraryRightThickness(this.getLibrarySize())} />
        {this.getLibrarySize() > 0 ? <span className='library-size' style={LibraryUiUtils.libraryHeight(this.getLibrarySize(), this.props.type)}>{ this.getLibrarySize() }</span> : null}
      </div>
    )
  }
}

const visibleCardClickAction = (cardId, event) => {
  return {
    type: 'VISIBLE_LIBRARY_CARD_CLICK',
    cardId: cardId,
    event: event
  }
}

const mapStateToProps = state => {
  return {
    playerLibrarySize: get(state, 'player.librarySize', 0),
    opponentLibrarySize: get(state, 'opponent.librarySize', 0),
    visibleLibrary: get(state, 'player.visibleLibrary', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    visibleCardClick: bindActionCreators(visibleCardClickAction, dispatch)
  }
}

Library.propTypes = {
  type: PropTypes.string.isRequired,
  playerLibrarySize: PropTypes.number.isRequired,
  opponentLibrarySize: PropTypes.number.isRequired,
  visibleLibrary: PropTypes.array.isRequired,
}

export default connect(mapStateToProps, mapDispatchToProps)(Library)