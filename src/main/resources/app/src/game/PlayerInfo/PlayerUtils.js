export default class PlayerUtils {
  static getActivePlayer(state) {
    if (state.turn.currentTurnPlayer === state.player.name) {
      return state.player
    } else {
      return state.opponent
    }
  }

  static updateActivePlayer(state, newActivePlayer) {
    const newActivePlayerClone = Object.assign({}, newActivePlayer)
    if (state.turn.currentTurnPlayer === state.player.name) {
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


    if (state.turn.currentTurnPlayer === state.player.name) {
      return true
    } else {
      return false
    }
  }
}