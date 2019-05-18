import React, {Fragment, PureComponent} from 'react'
import CardUtils from './CardUtils'

export class Modifiers extends PureComponent {
  constructor(props) {
    super(props);
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

  render() {
    return <Fragment>
      {this.summoningSickness()}
      {this.damage()}
      {this.powerToughness()}
    </Fragment>
  }
}
