import get from 'lodash/get'

export class TurnUtils  {
  static selectTarget(state, target) {
    if (!state.turn.targetsIds) {
      state.turn.targetsIds = []
    }

    if (typeof target === 'string') {
      state.turn.targetsIds.push(target)
    } else {
      state.turn.targetsIds.push(target.id)
    }
  }

  static selectCardToBePlayed(state, cardInstance, ability) {
    state.turn.cardIdSelectedToBePlayed = cardInstance.id
    state.turn.abilityToBePlayed = ability
  }

  static getCardIdSelectedToBePlayed(state) {
    return state.turn.cardIdSelectedToBePlayed
  }

  static getAbilityToBePlayed(state) {
    return get(state, 'turn.abilityToBePlayed.abilityType')
  }

  static getTargetsIds(state) {
    return state.turn.targetsIds
  }

  static resetTarget(state) {
    state.turn.cardIdSelectedToBePlayed = null
    state.turn.abilityToBePlayed = null
    state.turn.targetsIds = []
  }
}