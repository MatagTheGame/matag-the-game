import React, {PureComponent} from 'react';
import {connect} from 'react-redux'
import Card from '../Card'
import {get} from 'lodash'

class PlayerLibrary extends PureComponent {
  static libraryMarginBottom(cardsNumber) {
    const marginBottom = cardsNumber * 2
    return {
      marginBottom: `${marginBottom}px`,
    }
  }

  static cardBottomThickness(cardsNumber) {
    const halfCardsNumber = Math.floor(cardsNumber / 2)
    return {
      height: `${cardsNumber}px`,
      bottom: `-${halfCardsNumber}px`,
      transform: `rotateX(-45deg) translateY(${halfCardsNumber}px)`
    }
  }

  static cardRightThickness(cardsNumber) {
    const halfCardsNumber = Math.floor(cardsNumber / 2)
    return {
      width: `${cardsNumber}px`,
      right: `-${halfCardsNumber}px`,
      transform: `rotateY(109deg) translateX(${halfCardsNumber}px)`
    }
  }

  render() {
    return (
      <div id="player-library" style={PlayerLibrary.libraryMarginBottom(this.props.cards.length)}>
        {this.props.cards.length > 0 ? <Card name='card' /> : null}
        <div className='card-bottom-thickness' style={PlayerLibrary.cardBottomThickness(this.props.cards.length)} />
        <div className='card-right-thickness' style={PlayerLibrary.cardRightThickness(this.props.cards.length)} />
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