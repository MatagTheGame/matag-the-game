import React from 'react'
import {connect} from 'react-redux'
import get from 'lodash/get'
import {LibraryUiUtils} from './LibraryUiUtils'
import Card from '../Card/Card'
import PropTypes from 'prop-types'
import './library.scss'

function Library(props) {
  const getId = () => {
    return props.type + '-library'
  }
  
  const getLibrarySize = () => {
    if (props.type === 'player') {
      return props.playerLibrarySize
    } else {
      return props.opponentLibrarySize
    }
  }
  
  return (
    <div id={getId()} className='player-library'>
      {getLibrarySize() > 0 ? <Card style={LibraryUiUtils.libraryHeight(getLibrarySize(), props.type)} area='library' /> : null}
      <div className='card-bottom-thickness' style={LibraryUiUtils.libraryBottomThickness(getLibrarySize())} />
      <div className='card-right-thickness' style={LibraryUiUtils.libraryRightThickness(getLibrarySize())} />
      {getLibrarySize() > 0 ? <span className='library-size' style={LibraryUiUtils.libraryHeight(getLibrarySize(), props.type)}>{ getLibrarySize() }</span> : null}
    </div>
  )
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