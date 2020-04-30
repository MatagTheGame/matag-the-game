import React, {Component} from 'react'
import get from 'lodash/get'
import throttle from 'lodash/throttle'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'
import PropTypes from 'prop-types'
import CardUtils from './CardUtils'
import {Modifiers} from './Modifiers'
import './card.scss'

class Card extends Component {
  constructor(props) {
    super(props)
    this.onWheel = throttle(this.onWheel.bind(this))
  }

  name() {
    return get(this.props.cardInstance, 'card.name', 'card')
  }

  imageCardUrl() {
    const name = CardUtils.normalizeCardName(this.name())
    if (name === 'card') {
      return 'url("/img/card-back.jpg")'
    } else {
      return `url("${this.props.cardInstance.card.imageUrls[this.props.resolution]}")`
    }
  }

  isCurrentCardATarget() {
    return this.props.targetIdsForLastSpell.indexOf(this.props.cardInstance.id) >= 0
  }

  getClasses() {
    let classes = 'card'

    if (CardUtils.isTapped(this.props.cardInstance))  {
      if (CardUtils.doesNotUntapNextTurn(this.props.cardInstance)) {
        classes += ' tapped-does-not-untap-next-turn'
      } else {
        classes += ' tapped'
      }

    } else if (CardUtils.isFrontendTapped(this.props.cardInstance)) {
      classes += ' frontend-tapped'
    }

    if (CardUtils.isAttacking(this.props.cardInstance)) {
      classes += ' attacking'
    }

    if (CardUtils.isFrontendBlocking(this.props.cardInstance)) {
      classes += ' frontend-blocking'
    } else if (CardUtils.isBlocking(this.props.cardInstance)) {
      classes += ' blocking'
    }

    if (this.props.selected) {
      classes += ' selected'
    }

    if (CardUtils.hasAbility(this.props.cardInstance, 'FLYING')) {
      classes += ' flying'
    }

    if (this.isCurrentCardATarget()) {
      classes += ' targeted'
    }

    return classes
  }

  onWheel(e) {
    if (this.name() !== 'card') {
      if (e.deltaY < 0) {
        this.props.maximizeCard(this.imageCardUrl())
      }
    }
  }

  render() {
    if (this.props.cardInstance) {
      return (
        <div id={`card-${this.props.cardInstance.id}`}
          className={this.getClasses()}
          style={{backgroundImage: this.imageCardUrl(), ...this.props.style}}
          onClick={this.props.onclick}
          onWheel={this.onWheel}
          title={this.props.cardInstance.card.name}
          aria-label={this.props.cardInstance.card.name}>
          { this.props.area === 'battlefield' ? <Modifiers cardInstance={this.props.cardInstance} /> : null }
        </div>
      )
    } else {
      return <div className='card' style={{backgroundImage: this.imageCardUrl(), ...this.props.style}} />
    }
  }
}

const maximizeCardEvent = (cardImage) => {
  return {
    type: 'MAXIMIZE_MINIMIZE_CARD',
    value: {
      cardImage: cardImage
    }
  }
}

const mapStateToProps = state => {
  return {
    targetIdsForLastSpell: get(state, 'stack[0].modifiers.targets', []),
    resolution: get(state, 'player.resolution', 'lowResolution')
  }
}

const mapDispatchToProps = dispatch => {
  return {
    maximizeCard: bindActionCreators(maximizeCardEvent, dispatch)
  }
}

Card.propTypes = {
  targetIdsForLastSpell: PropTypes.array.isRequired,
  maximizeCard: PropTypes.func.isRequired,
  onClick: PropTypes.func,
  area: PropTypes.string,
  style: PropTypes.object,
  cardInstance: PropTypes.object,
  selected: PropTypes.bool,
  resolution: PropTypes.string
}

export default connect(mapStateToProps, mapDispatchToProps)(Card)
