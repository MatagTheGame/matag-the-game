import PlayerUtils from '../PlayerInfo/PlayerUtils'

export default class StatusMessageUtils {
  static getStatusMessageForUser(state) {
    if (PlayerUtils.isCurrentPlayerActive(state)) {
      switch (state.turn.triggeredAction) {
        case 'DISCARD_A_CARD':
          return 'Chose a card to discard.'
        default:
          return 'Play any spell or abilities or continue (SPACE).'
      }
    } else {
      return 'Wait for opponent to perform its action...'
    }
  }
}
