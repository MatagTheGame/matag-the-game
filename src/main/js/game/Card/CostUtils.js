import _ from 'lodash'
import get from 'lodash/get'
import CardSearch from './CardSearch'
import CardUtils from './CardUtils'

export default class CostUtils {
  static isCastingCostFulfilled(cardInstance, currentTappedMana) {
    return CostUtils.isCostFulfilled(cardInstance.card.cost, currentTappedMana)
  }

  static isAbilityCostFulfilled(cardInstance, ability, currentTappedMana) {
    const costWithoutTap = CostUtils.costWithoutTapping(ability.trigger.cost)
    const needsTapping = CostUtils.needsTapping(ability.trigger.cost)

    if (needsTapping) {
      if (CardUtils.isTapped(cardInstance) || CardUtils.hasSummoningSickness(cardInstance)) {
        return false
      }
    }

    return CostUtils.isCostFulfilled(costWithoutTap, currentTappedMana)
  }

  static isCostFulfilled(costToFulfill, currentTappedMana) {
    const manaPaid = _.flatten(_.values(currentTappedMana))
    for (const cost of costToFulfill) {
      let removed = false

      if (cost !== 'COLORLESS') {
        const index = manaPaid.indexOf(cost)
        if (index >= 0) {
          removed = true
          manaPaid.splice(index, 1)
        }

      } else {
        if (manaPaid.length > 0) {
          removed = true
          manaPaid.splice(0, 1)
        }
      }

      if (!removed) {
        return false
      }
    }

    return true
  }

  static needsTapping(cost) {
    return cost.find(c => c === 'TAP')
  }

  static costWithoutTapping(cost) {
    return cost.filter(c => c !== 'TAP')
  }

  static colorlessCost(cost) {
    return cost.filter(c => c === 'COLORLESS').length
  }

  static costWithoutColorless(cost) {
    return cost.filter(c => c !== 'COLORLESS')
  }

  static getMana(state) {
    return get(state, 'player.mana', {})
  }

  static addMana(state, cardId, manaTypes) {
    const mana = CostUtils.getMana(state)
    mana[cardId] = manaTypes
    state.player.mana = mana
  }

  static removeMana(state, cardId) {
    const mana = CostUtils.getMana(state)
    delete mana[cardId]
  }

  static clearMana(state) {
    for (let cardId in this.getMana(state)) {
      const cardInstance = CardSearch.cards(state.player.battlefield).withId(cardId)
      if (!CardUtils.isFrontendTapped(cardInstance)) {
        CostUtils.removeMana(state, cardId)
      }
    }
  }
}