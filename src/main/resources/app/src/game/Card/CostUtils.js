import CardSearch from './CardSearch'

export default class CostUtils {
  static currentMana(battlefield) {
    return CardSearch.cards(battlefield)
      .ofType('LAND')
      .frontEndTapped()
      .map(cardInstance => cardInstance.card.colors[0])
  }

  static currentTappedMana(battlefield) {
    const map = {}

    CardSearch.cards(battlefield)
      .ofType('LAND')
      .frontEndTapped()
      .forEach(cardInstance => {
        map[cardInstance.id] = [cardInstance.card.colors[0]]
      })

    return map
  }

  static isCastingCostFulfilled(card, manaPaid) {
    return CostUtils.isCostFulfilled(card.cost, manaPaid)
  }

  static isAbilityCostFulfilled(ability, manaPaid) {
    return CostUtils.isCostFulfilled(ability.trigger.cost, manaPaid)
  }

  static isCostFulfilled(costToFulfill, manaPaid) {
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