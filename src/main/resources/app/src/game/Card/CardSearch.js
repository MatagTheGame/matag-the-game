import CardUtils from './CardUtils'

export default class CardSearch extends Array {
  static cards(cardInstances) {
    return new CardSearch(...cardInstances)
  }

  withId(id) {
    return this.find(cardInstance => cardInstance.id === parseInt(id))
  }

  indexOfId(id) {
    return this.map(cardInstance => cardInstance.id).indexOf(parseInt(id))
  }

  ofType(type) {
    const cards = this.filter(cardInstance => CardUtils.isOfType(cardInstance, type))
    return new CardSearch(...cards)
  }

  frontEndTapped() {
    const cards = this.filter(cardInstance => CardUtils.isFrontendTapped(cardInstance))
    return new CardSearch(...cards)
  }

  untapped() {
    const cards = this.filter(cardInstance => CardUtils.isUntapped(cardInstance))
    return new CardSearch(...cards)
  }

  withoutSummoningSickness() {
    const cards = this.filter(cardInstance => !CardUtils.hasSummoningSickness(cardInstance))
    return new CardSearch(...cards)
  }

  frontEndAttacking() {
    const cards = this.filter(cardInstance => CardUtils.isFrontendAttacking(cardInstance))
    return new CardSearch(...cards)
  }

  frontEndBlocking() {
    const cards = this.filter(cardInstance => CardUtils.isFrontendBlocking(cardInstance))
    return new CardSearch(...cards)
  }

  attacking() {
    const cards = this.filter(cardInstance => CardUtils.isAttacking(cardInstance))
    return new CardSearch(...cards)
  }

  attackingOrFrontendAttacking() {
    const cards = this.filter(cardInstance => CardUtils.isAttackingOrFrontendAttacking(cardInstance))
    return new CardSearch(...cards)
  }

  blocking() {
    const cards = this.filter(cardInstance => CardUtils.isBlocking(cardInstance))
    return new CardSearch(...cards)
  }

  blockingOrFrontenBlocking() {
    const cards = this.filter(cardInstance => CardUtils.isBlockingOrFrontendBlocking(cardInstance))
    return new CardSearch(...cards)
  }

  attackingOrBlocking() {
    const cards = this.filter(cardInstance => CardUtils.isAttackingOrFrontendAttacking(cardInstance) || CardUtils.isBlockingOrFrontendBlocking(cardInstance))
    return new CardSearch(...cards)
  }

  notAttackingOrBlocking() {
    const cards = this.filter(cardInstance => CardUtils.isNotAttackingOrFrontendAttacking(cardInstance) && CardUtils.isNotBlockingOrFrontendBlocking(cardInstance))
    return new CardSearch(...cards)
  }

  isEmpty() {
    return this.length === 0
  }

  isNotEmpty() {
    return this.length > 0
  }

  concat(array) {
    const cards = super.concat(array)
    return new CardSearch(...cards)
  }
}
