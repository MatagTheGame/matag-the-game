import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import CostUtils from 'game/Card/CostUtils'
import UserInputs from 'game/UserInterface/UserInputs'

class PlayableAbilities extends Component {
  constructor(props) {
    super(props)
    this.playAbility = this.playAbility.bind(this)
  }

  playAbility(index) {
    this.props.playAbilitiesAction(this.props.cardId, index)
  }

  static renderCost(cost) {
    const needsTapping = CostUtils.needsTapping(cost)
    cost = CostUtils.costWithoutTapping(cost)

    const anyColorCost = CostUtils.anyColorCost(cost)
    cost = CostUtils.costWithoutAnyColor(cost)

    return (
      <>
        { anyColorCost > 0 && PlayableAbilities.renderSymbols([anyColorCost]) }
        { PlayableAbilities.renderSymbols(cost) }
        { anyColorCost > 0 && cost.length > 0 && needsTapping && ', ' }
        { needsTapping && PlayableAbilities.renderSymbols(['TAP']) }
      </>
    )
  }

  static renderSymbols(symbols) {
    return symbols.map((symbol, index) => <img key={index} src={`/img/symbols/${symbol}.png`} alt={symbol} />)
  }

  component(possibleAbility) {
    switch (possibleAbility.trigger.type) {
      case 'MANA_ABILITY':
        const colors = possibleAbility.parameters
        return <>{PlayableAbilities.renderCost(['TAP'])}: add {PlayableAbilities.renderSymbols(colors)}</>

      case 'ACTIVATED_ABILITY':
        const cost = possibleAbility.trigger.cost
        return <>{PlayableAbilities.renderCost(cost)}: {possibleAbility.abilityTypeText}</>
    }
  }

  render() {
    const userOptions = this.props.possibleAbilities.map(possibleAbility => {
      return {
        title: possibleAbility.abilityTypeText,
        component: this.component(possibleAbility),
        func: this.playAbility
      }
    })

    return <UserInputs userOptions={userOptions}/>
  }
}

const playAbilitiesAction = (cardId, index) => {
  return {
    type: 'PLAY_ABILITIES_CLICK',
    cardId: cardId,
    index: index
  }
}

const mapStateToProps = state => {
  return {
    cardId: get(state, 'userInterface.playableAbilities.cardId', -1),
    possibleAbilities: get(state, 'userInterface.playableAbilities.possibleAbilities', [])
  }
}

const mapDispatchToProps = dispatch => {
  return {
    playAbilitiesAction: bindActionCreators(playAbilitiesAction, dispatch)
  }
}

PlayableAbilities.propTypes = {
  cardId: PropTypes.number.isRequired,
  possibleAbilities: PropTypes.array.isRequired,
}


export default connect(mapStateToProps, mapDispatchToProps)(PlayableAbilities)