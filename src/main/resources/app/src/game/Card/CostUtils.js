export default class CostUtils {
  static currentMana(battlefield) {
    return battlefield
      .filter(cardInstance => cardInstance.card.types.includes('LAND'))
      .filter(cardInstance => cardInstance.modifiers.tapped === 'FRONTEND_TAPPED')
      .map(cardInstance => cardInstance.card.colors[0])
  }

  static currentManaCardIds(battlefield) {
    return battlefield
      .filter(cardInstance => cardInstance.card.types.includes('LAND'))
      .filter(cardInstance => cardInstance.modifiers.tapped === 'FRONTEND_TAPPED')
      .map(cardInstance => cardInstance.id)
  }

  static isCastingCostFulfilled(card, manaPaid) {
    for (const cost of card.cost) {
      let removed = false

      if (cost !== 'COLORLESS') {
        const index = manaPaid.indexOf(cost)
        if (index >= 0) {
          removed = true
          manaPaid = manaPaid.splice(index, 1)
        }

      } else {
        if (manaPaid.length > 0) {
          removed = true
          manaPaid = manaPaid.splice(0, 1)
        }
      }

      if (!removed) {
        return false
      }
    }

    return true
  }
}