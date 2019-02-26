export default class PlayerUtils {
  static isCurrentPlayerActive(state) {
    return state.turn.currentTurnPlayer === state.player.name
  }

  static getActivePlayer(state) {
    if (PlayerUtils.isCurrentPlayerActive(state)) {
      return state.player
    } else {
      return state.opponent
    }
  }

  static updateActivePlayer(state, newActivePlayer) {
    const newActivePlayerClone = Object.assign({}, newActivePlayer)
    if (PlayerUtils.isCurrentPlayerActive(state)) {
      return Object.assign(state, {player: newActivePlayerClone})
    } else {
      return Object.assign(state, {opponent: newActivePlayerClone})
    }
  }

  static canActivePlayerPerformAnyAction(state) {
    if (!state.turn) {
      return true
    }

    // FIXME for the time being assuming that current player can always perform an action and opponent never


    if (PlayerUtils.isCurrentPlayerActive(state)) {
      return true
    } else {
      return false
    }
  }
}