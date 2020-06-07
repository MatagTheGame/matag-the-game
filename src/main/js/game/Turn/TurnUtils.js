import get from 'lodash/get'

export default class TurnUtils  {
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

  static selectCardToBePlayed(state, cardInstance, abilities) {
    state.turn.cardIdSelectedToBePlayed = cardInstance.id
    state.turn.abilitiesToBePlayed = abilities
  }

  static getCardIdSelectedToBePlayed(state) {
    return state.turn.cardIdSelectedToBePlayed
  }

  static getAbilitiesToBePlayed(state) {
    return get(state, 'turn.abilitiesToBePlayed', [])
      .filter(ability => get(ability, 'trigger.type') === 'ACTIVATED_ABILITY')
      .map(ability => ability.abilityType)
  }

  static getTargetsIds(state) {
    return get(state, 'turn.targetsIds', [])
  }

  static resetTarget(state) {
    state.turn.cardIdSelectedToBePlayed = null
    state.turn.abilityToBePlayed = null
    state.turn.targetsIds = []
  }
}