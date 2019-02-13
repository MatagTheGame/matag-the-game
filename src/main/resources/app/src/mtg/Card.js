import React, {PureComponent} from 'react';

export default class Card extends PureComponent {
  imageUrl () {
    if (this.props.name === 'card') {
      return 'url("/img/card-back.jpg")'
    } else {
      return `url("/img/cards/${this.props.name}.jpg")`
    }
  }

  render() {
    return (
      <div className="card" style={{backgroundImage: this.imageUrl()}}/>
    )
  }
}
