import React, {PureComponent} from 'react'
import {get} from 'lodash'
import CardUtils from './CardUtils'

export default class Card extends PureComponent {
  constructor(props) {
    super(props);
    this.state = {
      maximized: false
    }
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

  getClasses() {
    let classes = 'card'

    if (get(this.props.cardInstance, 'modifiers.tapped')) {
      classes += ' ' + this.props.cardInstance.modifiers.tapped.toLowerCase()
    }

    if (this.state.maximized) {
      classes += ' maximized'
    }

    return classes
  }

  onWheel(e) {
    if (e.deltaY > 0) {
      if (this.state.maximized) {
        this.setState({maximized: false})
      }
    }
    if (e.deltaY < 0) {
      if (!this.state.maximized) {
        this.setState({maximized: true})
      }
    }
  }

  render() {
    if (this.props.cardInstance) {
      return <div id={'card-' + this.props.cardInstance.id}
                  className={this.getClasses()}
                  style={{backgroundImage: this.imageUrl(), ...this.props.style}}
                  onClick={this.props.onclick}
                  onWheel={this.onWheel}/>
    } else {
      return <div className='card' style={{backgroundImage: this.imageUrl(), ...this.props.style}} />
    }
  }
}
