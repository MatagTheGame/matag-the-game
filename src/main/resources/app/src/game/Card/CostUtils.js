import _ from 'lodash'
import CardSearch from './CardSearch'
import CardUtils from 'Main/game/Card/CardUtils'

export default class CostUtils {
  static currentTappedMana(battlefield) {
    const map = {}

    CardSearch.cards(battlefield)
      .withAbility('TAP_ADD_MANA')
      .frontEndTapped()
      .forEach(cardInstance => {
        map[cardInstance.id] = CardUtils.getAbilities(cardInstance, 'TAP_ADD_MANA')[0].parameters[0]
      })

    return map
  }

  static isCastingCostFulfilled(card, currentTappedMana) {
    return CostUtils.isCostFulfilled(card.cost, currentTappedMana)
  }

  static isAbilityCostFulfilled(ability, currentTappedMana) {
    return CostUtils.isCostFulfilled(ability.trigger.cost, currentTappedMana)
  }

  static isCostFulfilled(costToFulfill, currentTappedMana) {
    const manaPaid = _.values(currentTappedMana)
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
}