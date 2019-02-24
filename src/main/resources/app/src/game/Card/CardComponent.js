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

  imageUrl () {
    const name = CardComponent.normalizeCardName(this.props.name)
    if (name === 'card') {
      return 'url("/img/card-back.jpg")'
    } else {
      return `url("/img/cards/${name}.jpg")`
    }
  }

  render() {
    return (
      <div id={'card-' + this.props.id}
           className="card"
           style={{backgroundImage: this.imageUrl(), ...this.props.style}}
           onClick={this.props.onclick} />
    )
  }
}
