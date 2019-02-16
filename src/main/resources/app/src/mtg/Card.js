import React, {PureComponent} from 'react';

export default class Card extends PureComponent {
  static normalizeCardName(cardName) {
    return cardName.toLowerCase()
      .replace(' ', '_')
      .replace(',', '_')
  }

  imageUrl () {
    const name = Card.normalizeCardName(this.props.name)
    if (name === 'card') {
      return 'url("/img/card-back.jpg")'
    } else {
      return `url("/img/cards/${name}.jpg")`
    }
  }

  render() {
    return (
      <div className="card" style={{backgroundImage: this.imageUrl()}} />
    )
  }
}
