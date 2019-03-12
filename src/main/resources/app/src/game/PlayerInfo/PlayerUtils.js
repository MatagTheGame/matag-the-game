import Phase from '../Turn/Phase'
import CardSearch from '../Card/CardSearch'

export default class PlayerUtils {
  static isCurrentPlayerTurn(state) {
    return state.turn.currentTurnPlayer === state.player.name
  }

  static isCurrentPlayerActive(state) {
    return state.turn.currentPhaseActivePlayer === state.player.name
  }

  static getPlayerByName(state, playerName) {
    if (state.player.name === playerName) {
      return state.player
    } else {
      return state.opponent
    }
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
      state.statusMessage = "Wait for opponent to perform its action..."
      return false

    } else {
      if (Phase.isMainPhase(state.turn.currentPhase)) {
        state.statusMessage = "Play any spell or abilities or continue (SPACE)."
        return true
      } else if (state.turn.triggeredAction) {
        state.statusMessage = "Chose a card to discard."
        return true
      } else if (state.turn.currentPhase === 'DA' && PlayerUtils.isPlayerAbleToAttack(state)) {
        state.statusMessage = "Choose creatures you want to attack with."
        return true
      } else {
        state.statusMessage = "..."
        return false
      }
    }
  }
}