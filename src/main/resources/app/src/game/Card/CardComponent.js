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

  static toggleFrontendTapped(cardInstance) {
    if (CardComponent.isFrontendTapped(cardInstance)) {
      cardInstance.modifiers.tapped = undefined
    } else {
      cardInstance.modifiers.tapped = 'FRONTEND_TAPPED'
    }
  }

  static isFrontendTapped(cardInstance) {
    return cardInstance.modifiers.tapped === 'FRONTEND_TAPPED'
  }

  static untapAllFrontendTappedCards(cards) {
    cards.filter(card => CardComponent.isFrontendTapped(card))
      .forEach((card) => CardComponent.toggleFrontendTapped(card))
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
