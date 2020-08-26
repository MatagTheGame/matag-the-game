import PlayerUtils from 'game/PlayerInfo/PlayerUtils'
import UserInterfaceUtils from 'game/UserInterface/UserInterfaceUtils'
import CostUtils from 'game/Card/CostUtils'

export default class ServerEventsReducer {
  static getEvents() {
    return ['HEALTHCHECK', 'MESSAGE', 'INIT_WAITING_OPPONENT', 'OPPONENT_JOINED', 'INIT_PLAYER_AND_OPPONENT', 'UPDATE_GAME_STATUS']
  }

  static reduceEvent(newState, action) {
    switch (action.type) {
    case 'HEALTHCHECK':
      UserInterfaceUtils.setLastHealthcheckReceived(newState)
      break

    case 'MESSAGE':
      UserInterfaceUtils.setMessage(newState, action.value.text, action.value.closable)
      break

    case 'INIT_WAITING_OPPONENT':
      UserInterfaceUtils.setMessage(newState, 'Waiting for opponent...', false)
      break

    case 'OPPONENT_JOINED':
      UserInterfaceUtils.unsetMessage(newState)
      break

    case 'INIT_PLAYER_AND_OPPONENT':
      newState.player = action.value.player
      newState.opponent = action.value.opponent
      break

    case 'UPDATE_GAME_STATUS':
      const updateEvent = action.value.value

      newState.turn = updateEvent.turn
      newState.stack = updateEvent.stack

      for (let i = 0; i < updateEvent.playersUpdateEvents.length; i++) {
        const playerUpdateEvent = updateEvent.playersUpdateEvents[i]
        const player = PlayerUtils.getPlayerByName(newState, playerUpdateEvent.name)
        player.life = playerUpdateEvent.life
        player.librarySize = playerUpdateEvent.librarySize - playerUpdateEvent.visibleLibrary.length
        player.visibleLibrary = playerUpdateEvent.visibleLibrary
        player.battlefield = playerUpdateEvent.battlefield
        player.graveyard = playerUpdateEvent.graveyard
        player.hand = playerUpdateEvent.hand
      }

      newState.turn.blockingCardPosition = 0
      CostUtils.clearMana(newState)
      UserInterfaceUtils.computeStatusMessage(newState)
      break
    }

    return newState
  }
}