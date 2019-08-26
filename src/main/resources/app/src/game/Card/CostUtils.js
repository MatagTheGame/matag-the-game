import _ from 'lodash'
import get from 'lodash/get'
import CardSearch from 'Main/game/Card/CardSearch'
import CardUtils from 'Main/game/Card/CardUtils'

export default class CostUtils {
  static isCastingCostFulfilled(card, currentTappedMana) {
    return CostUtils.isCostFulfilled(card.cost, currentTappedMana)
  }

  static isAbilityCostFulfilled(ability, currentTappedMana) {
    return CostUtils.isCostFulfilled(ability.trigger.cost, currentTappedMana)
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