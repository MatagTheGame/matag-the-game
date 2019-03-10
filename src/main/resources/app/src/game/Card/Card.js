import React, {PureComponent} from 'react';
import CardUtils from './CardUtils'

export default class Card extends PureComponent {
  imageUrl() {
    const name = CardUtils.normalizeCardName(this.props.name)
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
