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

  static getCurrentPlayer(state) {
    return PlayerUtils.getPlayerByName(state, state.turn.currentTurnPlayer)
  }

  static getNonCurrentPlayer(state) {
    if (PlayerUtils.isCurrentPlayerTurn(state)) {
      return state.opponent
    } else {
      return state.player
    }
  }

  static getActivePlayer(state) {
    return PlayerUtils.getPlayerByName(state, state.turn.currentPhaseActivePlayer)
  }

  static isPlayerAbleToAttack(state) {
    const battlefield = PlayerUtils.getCurrentPlayer(state).battlefield
    return CardSearch.cards(battlefield)
      .ofType('CREATURE')
      .withoutSummoningSickness()
      .untapped()
      .isNotEmpty()
  }

  static isPlayerAbleToBlock(state) {
    const battlefield = PlayerUtils.getActivePlayer(state).battlefield
    return CardSearch.cards(battlefield)
      .ofType('CREATURE')
      .untapped()
      .isNotEmpty()
  }

  static canPlayerPerformAnyAction(state) {
    if (!PlayerUtils.isCurrentPlayerTurn(state)) {
      if (state.turn.currentPhase === 'DB' && PlayerUtils.isPlayerAbleToBlock(state)) {
        state.statusMessage = "Choose creatures you want to block with."
        return true
      } else {
        state.statusMessage = "Wait for opponent to perform its action..."
        return false
      }

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