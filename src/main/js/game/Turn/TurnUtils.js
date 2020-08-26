import get from 'lodash/get'
import Phase from 'game/Turn/Phase'

export default class TurnUtils  {
  static phasesToRender(currentPhase) {
    let phases = Phase.getPhases()
    const activePhaseIndex = Phase.getPhases().indexOf(currentPhase)

    if (activePhaseIndex < 3) {
      return [...phases.splice(0, 4), '...']
    }

    if (activePhaseIndex > phases.length - 4) {
      return ['...', ...phases.splice(phases.length - 4, 4)]
    }

    return ['...', ...phases.splice(activePhaseIndex - 1, 3), '...']
  }

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

  static inputRequiredActionIs(state, action) {
    return state.turn.inputRequiredAction === action
  }

  static resetTarget(state) {
    state.turn.cardIdSelectedToBePlayed = null
    state.turn.abilityToBePlayed = null
    state.turn.targetsIds = []
  }
}