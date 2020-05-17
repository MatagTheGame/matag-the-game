import React, {Component} from 'react'
import {connect} from 'react-redux'
import {bindActionCreators} from 'redux'
import get from 'lodash/get'
import PropTypes from 'prop-types'
import './playableAbilities.scss'
import CostUtils from 'game/Card/CostUtils'

class PossibleAbility extends Component {
  static renderCost(cost) {
    const needsTapping = CostUtils.needsTapping(cost)
    cost = CostUtils.costWithoutTapping(cost)

    const colorlessCost = CostUtils.colorlessCost(cost)
    cost = CostUtils.costWithoutColorless(cost)

    return (
      <>
        { colorlessCost > 0 && PossibleAbility.renderSymbols([colorlessCost]) }
        { PossibleAbility.renderSymbols(cost) }
        { colorlessCost > 0 && cost.length > 0 && needsTapping && ', ' }
        { needsTapping && PossibleAbility.renderSymbols(['TAP']) }
      </>
    )
  }

  static renderSymbols(symbols) {
    return symbols.map((symbol, index) => <img key={index} src={`/img/symbols/${symbol}.png`} alt={symbol} />)
  }

  render() {
    switch (this.props.possibleAbility.trigger.type) {
    case 'MANA_ABILITY':
      const colors = this.props.possibleAbility.parameters
      return <li onClick={this.props.onClick} title={this.props.possibleAbility.abilityTypeText}>{PossibleAbility.renderCost(['TAP'])}: add {PossibleAbility.renderSymbols(colors)}</li>
    case 'ACTIVATED_ABILITY':
      const cost = this.props.possibleAbility.trigger.cost
      return <li onClick={this.props.onClick} title={this.props.possibleAbility.abilityTypeText}>{PossibleAbility.renderCost(cost)}: {this.props.possibleAbility.abilityTypeText}</li>
    default:
      throw new Error('Trigger type for ability not recognised: ' + JSON.stringify(this.props.possibleAbility))
    }
  }
}

PossibleAbility.propTypes = {
  possibleAbility: PropTypes.object.isRequired,
  onClick: PropTypes.func.isRequired
}

class PlayableAbilities extends Component {
  constructor(props) {
    super(props)
    this.closePlayableAbilitiesOverlay = this.closePlayableAbilitiesOverlay.bind(this)
    this.playAbility = this.playAbility.bind(this)
  }

  playAbility(index) {
    this.props.playAbilitiesAction(this.props.cardId, index)
  }

  closePlayableAbilitiesOverlay() {
    this.props.closePlayableAbilitiesOverlay()
  }

  render() {
    if (this.props.possibleAbilities.length > 1) {
      return (
        <div id='playable-abilities-overlay' onClick={this.closePlayableAbilitiesOverlay}>
          <div id='playable-abilities' style={{left: this.props.position.x + 'px', top: (this.props.position.y - 150) + 'px'}}>
            <ul>
              { this.props.possibleAbilities.map((possibleAbility, index) =>
                <PossibleAbility key={index} possibleAbility={possibleAbility} onClick={() => this.playAbility(index)} />) }
            </ul>
          </div>
        </div>
      )
    } else {
      return null
    }
  }
}

const closePlayableAbilitiesAction = () => {
  return {
    type: 'CLOSE_PLAYABLE_ABILITIES_CLICK'
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
    possibleAbilities: get(state, 'userInterface.playableAbilities.possibleAbilities', []),
    position: get(state, 'userInterface.playableAbilities.position', {x: -1, y: -1})
  }
}

const mapDispatchToProps = dispatch => {
  return {
    closePlayableAbilitiesOverlay: bindActionCreators(closePlayableAbilitiesAction, dispatch),
    playAbilitiesAction: bindActionCreators(playAbilitiesAction, dispatch)
  }
}

PlayableAbilities.propTypes = {
  cardId: PropTypes.number.isRequired,
  possibleAbilities: PropTypes.array.isRequired,
  position: PropTypes.object.isRequired,
}


export default connect(mapStateToProps, mapDispatchToProps)(PlayableAbilities)