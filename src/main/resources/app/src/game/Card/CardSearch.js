export default class CardSearch extends Array {
  static cards(cardInstances) {
    return new CardSearch(...cardInstances)
  }

  withId(id) {
    return this.find(cardInstance => cardInstance.id === parseInt(id))
  }

  ofType(type) {
    const cards = this.filter(cardInstance => cardInstance.card.types.includes(type))
    return new CardSearch(...cards)
  }

  frontEndTapped() {
    const cards = this.filter(cardInstance => cardInstance.modifiers.tapped === 'FRONTEND_TAPPED')
    return new CardSearch(...cards)
  }

  withoutSummoningSickness() {
    const cards = this.filter(cardInstance => !cardInstance.modifiers.summoningSickness)
    return new CardSearch(...cards)
  }

  isEmpty() {
    return this.length === 0
  }

  isNotEmpty() {
    return this.length > 0
  }
}
