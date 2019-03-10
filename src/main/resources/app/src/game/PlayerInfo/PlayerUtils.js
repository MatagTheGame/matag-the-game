import Phase from '../Turn/Phase'
import CardSearch from '../Card/CardSearch'

export default class PlayerUtils {
  static isCurrentPlayerTurn(state) {
    return state.turn.currentTurnPlayer === state.player.name
  }

  static isCurrentPlayerActive(state) {
    return state.turn.currentPhaseActivePlayer === state.player.name
  }

  static getActivePlayer(state) {
    if (PlayerUtils.isCurrentPlayerTurn(state)) {
      return state.player
    } else {
      return state.opponent
    }
  }

  static isPlayerAbleToAttack(state) {
    const battlefield = PlayerUtils.getActivePlayer(state).battlefield
    return CardSearch.cards(battlefield)
      .ofType('CREATURE')
      .withoutSummoningSickness()
      .isNotEmpty()
  }

  static canPlayerPerformAnyAction(state) {
    if (!PlayerUtils.isCurrentPlayerTurn(state)) {
      // TODO until instants and abilities are implemented opponent cannot do anything during player phases
      return false

    } else {
      if (Phase.isMainPhase(state.turn.currentPhase)) {
        return true
      } else if (state.turn.triggeredAction) {
        return true
      } else if (state.turn.currentPhase === 'DA' && PlayerUtils.isPlayerAbleToAttack(state)) {
        return true
      } else {
        return false
      }
    }
  }
}