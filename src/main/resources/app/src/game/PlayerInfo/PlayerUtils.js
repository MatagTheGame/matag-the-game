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

  static canActivePlayerPerformAnyAction(state) {
    if (!state.turn) {
      return true
    }

    if (PlayerUtils.isCurrentPlayerTurn(state)) {
      if (state.turn.triggeredAction) {
        return true
      } else {
        return false
      }
    } else {
      return false
    }
  }
}