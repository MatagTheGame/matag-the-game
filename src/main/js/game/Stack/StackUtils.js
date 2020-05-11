export default class StackUtils {
  static isStackEmpty(state) {
    return !state.stack || state.stack.length === 0
  }

  static isACastedCard(cardInstance) {
    return cardInstance.triggeredAbilities.length === 0
  }

  static getTopSpell(state) {
    if (state.stack) {
      return state.stack[state.stack.length - 1]
    }
  }
}