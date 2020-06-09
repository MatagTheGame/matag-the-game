import React, {Component} from 'react'
import CardUtils from './CardUtils'

class Counters extends Component {
  constructor(props) {
    super(props)
  }

  plus1Counters() {
    const plus1Counters = this.props.counters.plus1Counters
    if (plus1Counters > 0) {
      return <div className={'counter plus-1-counter'}>{plus1Counters}</div>
    }
  }

  keywordCounters() {
    return this.props.counters.keywordCounters
      .map(keyword => keyword.toLowerCase())
      .map(keyword => <div key={keyword} className={'counter keyword-counter'} title={keyword} style={{'background-image': `url("/img/abilities/${keyword}.png")`}} >&nbsp;&nbsp;</div>)
  }

  render() {
    return <div className={'counters'}>
      {this.plus1Counters()}
      {this.keywordCounters()}
    </div>
  }
}

export class Modifiers extends Component {
  constructor(props) {
    super(props)
  }

  summoningSickness() {
    if (CardUtils.hasSummoningSickness(this.props.cardInstance)) {
      return <div className='summoning-sickness'/>
    }
  }

  damage() {
    const damage = CardUtils.getDamage(this.props.cardInstance)
    if (damage) {
      return <div className='damage'>{damage}</div>
    }
  }

  powerToughness() {
    if (CardUtils.isOfType(this.props.cardInstance, 'CREATURE')) {
      return <div className='power-toughness'>
        {CardUtils.getPower(this.props.cardInstance)}
        /
        {CardUtils.getToughness(this.props.cardInstance)}
      </div>
    }
  }

  counters() {
    return <Counters counters={CardUtils.getCounters(this.props.cardInstance)} />
  }

  render() {
    return <>
      {this.summoningSickness()}
      {this.counters()}
      {this.damage()}
      {this.powerToughness()}
    </>
  }
}
