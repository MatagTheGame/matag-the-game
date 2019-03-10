import React, {PureComponent} from 'react';

export default class CardComponent extends PureComponent {
  static normalizeCardName(cardName) {
    return cardName.toLowerCase()
      .replace(' ', '_')
      .replace(',', '_')
  }

  static findCardInstanceById (cards, cardId) {
    return cards.find(card => card.id === parseInt(cardId))
  }

  static extractCardId (id) {
    return id.replace('card-', '')
  }

  static frontendTap(cardInstance) {
    cardInstance.modifiers.tapped = 'FRONTEND_TAPPED'
  }

  static untap(cardInstance) {
    cardInstance.modifiers.tapped = undefined
  }

  static isFrontendTapped(cardInstance) {
    return cardInstance.modifiers.tapped === 'FRONTEND_TAPPED'
  }

  static isUntapped(cardInstance) {
    return !cardInstance.modifiers.tapped
  }

  static untapAllFrontendTappedCards(cardInstances) {
    console.log(cardInstances)
    cardInstances.filter(cardInstance => CardComponent.isFrontendTapped(cardInstance))
      .forEach((cardInstance) => CardComponent.untap(cardInstance))
  }

  imageUrl () {
    const name = CardComponent.normalizeCardName(this.props.name)
    if (name === 'card') {
      return 'url("/img/card-back.jpg")'
    } else {
      return `url("/img/cards/${name}.jpg")`
    }
  }

  getClasses() {
    let classes = 'card'
    if (this.props.tapped) {
      classes += ' ' + this.props.tapped.toLowerCase()
    }
    return classes
  }


  render() {
    return (
      <div id={'card-' + this.props.id}
           className={this.getClasses()}
           style={{backgroundImage: this.imageUrl(), ...this.props.style}}
           onClick={this.props.onclick} />
    )
  }
}
