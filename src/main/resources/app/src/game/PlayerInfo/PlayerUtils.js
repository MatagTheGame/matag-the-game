export default class PlayerUtils {
  static getActivePlayer(state) {
    if (state.turn.currentTurnPlayer === state.player.name) {
      return state.player
    } else {
      return state.opponent
    }
  }

  static updateActivePlayer(state, newActivePlayer) {
    if (state.turn.currentTurnPlayer === state.player.name) {
      return Object.assign(state, {player: newActivePlayer})
    } else {
      return Object.assign(state, {opponent: newActivePlayer})
    }
  }
}