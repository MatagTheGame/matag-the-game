export default class CardUtils {
  static normalizeCardName(cardName) {
    return cardName.toLowerCase()
      .replace(' ', '_')
      .replace(',', '_')
  }

  static findCardInstanceById(cards, cardId) {
    return cards.find(card => card.id === parseInt(cardId))
  }

  static extractCardId(id) {
    return id.replace('card-', '')
  }

  static frontendTap(cardInstance) {
    cardInstance.modifiers.tapped = 'FRONTEND_TAPPED'
  }

  static untap(cardInstance) {
    cardInstance.modifiers.tapped = undefined
  }

  static isFrontendTapped(cardInstance) {
    return cardInstance.modifiers.tapped === 'FRONTEND_TAPPED'
  }

  static isUntapped(cardInstance) {
    return !cardInstance.modifiers.tapped
  }

  static untapAllFrontendTappedCards(battlefield) {
    battlefield.filter(cardInstance => CardUtils.isFrontendTapped(cardInstance))
      .forEach((cardInstance) => CardUtils.untap(cardInstance))
  }
}
