import React, {Component, Fragment} from 'react'
import CardUtils from './CardUtils'

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

  plus1Counters() {
    let plus1Counters = CardUtils.getPlus1Counters(this.props.cardInstance)
    if (plus1Counters > 0) {
      return <div className={'plus-1-counters'}>{plus1Counters}</div>
    }
  }

  render() {
    return <Fragment>
      {this.summoningSickness()}
      {this.plus1Counters()}
      {this.damage()}
      {this.powerToughness()}
    </Fragment>
  }
}
