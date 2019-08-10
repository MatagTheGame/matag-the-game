import React, {Component, Fragment} from 'react'
import PropTypes from 'prop-types'

export class TriggeredAbility extends Component {
  constructor(props) {
    super(props)
  }

  getTriggeredAbilityText() {
    const triggeredAbility = this.props.cardInstance.triggeredAbilities[0]
    return (
      <Fragment>
        <strong>{this.props.cardInstance.controller}'s {this.props.cardInstance.card.name} ({this.props.cardInstance.id}):</strong><br/>
        {triggeredAbility.abilityTypesText}
      </Fragment>
    )
  }

  render() {
    return (
      <div className='triggered-ability'>
        {this.getTriggeredAbilityText()}
      </div>
    )
  }
}

TriggeredAbility.propTypes = {
  cardInstance: PropTypes.object.isRequired
}
