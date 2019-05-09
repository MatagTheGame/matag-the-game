import React, {PureComponent} from 'react'
import {get} from 'lodash'
import CardUtils from './CardUtils'
import {bindActionCreators} from 'redux'
import {connect} from 'react-redux'

class Card extends PureComponent {
  constructor(props) {
    super(props);
    this.onWheel= this.onWheel.bind(this);
  }

  name() {
    return get(this.props.cardInstance, 'card.name', 'card')
  }

  imageUrl() {
    const name = CardUtils.normalizeCardName(this.name())
    if (name === 'card') {
      return 'url("/img/card-back.jpg")'
    } else {
      return `url("/img/cards/${name}.jpg")`
    }
  }

  isCurrentCardATarget() {
    return this.props.targetIdsForLastSpell.includes(this.props.cardInstance.id)
  }

  getClasses() {
    let classes = 'card'

    if (CardUtils.isTapped(this.props.cardInstance))  {
      classes += ' tapped'
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
    const name = CardUtils.normalizeCardName(this.name())
    if (name !== 'card') {
      if (e.deltaY < 0) {
        this.props.maximizeCard(this.imageUrl())
      }
    }
  }

  modifiers() {
    if (CardUtils.hasSummoningSickness(this.props.cardInstance)) {
      return <div className='summoning-sickness'/>
    }
  }

  render() {
    if (this.props.cardInstance) {
      return (
        <div id={'card-' + this.props.cardInstance.id}
             className={this.getClasses()}
             style={{backgroundImage: this.imageUrl(), ...this.props.style}}
             onClick={this.props.onclick}
             onWheel={this.onWheel}>
          {this.modifiers()}
        </div>
      )
    } else {
      return <div className='card' style={{backgroundImage: this.imageUrl(), ...this.props.style}} />
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
    message: get(state, 'message', {}),
    targetIdsForLastSpell: get(state, 'stack[0].modifiers.targets', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    maximizeCard: bindActionCreators(maximizeCardEvent, dispatch)
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(Card)
