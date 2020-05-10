import PlayerUtils from 'game/PlayerInfo/PlayerUtils'
import UserInterfaceUtils from 'game/UserInterface/UserInterfaceUtils'
import CostUtils from 'game/Card/CostUtils'

export default class ServerEventsReducer {
  static getEvents() {
    return ['MESSAGE', 'INIT_WAITING_OPPONENT', 'OPPONENT_JOINED', 'INIT_PLAYER_AND_OPPONENT', 'UPDATE_GAME_STATUS', 'UPDATE_PLAYER_HAND']
  }

  static reduceEvent(newState, action) {
    let player
    switch (action.type) {
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
      newState.turn = action.value.turn
      newState.stack = action.value.stack

      for (let i = 0; i < action.value.playersUpdateEvents.length; i++) {
        const playerUpdateEvent = action.value.playersUpdateEvents[i]
        player = PlayerUtils.getPlayerByName(newState, playerUpdateEvent.name)
        player.life = playerUpdateEvent.life
        player.librarySize = playerUpdateEvent.librarySize
        player.battlefield = playerUpdateEvent.battlefield
        player.graveyard = playerUpdateEvent.graveyard
      }

      newState.turn.blockingCardPosition = 0
      CostUtils.clearMana(newState)
      UserInterfaceUtils.computeStatusMessage(newState)
      break

    case 'UPDATE_PLAYER_HAND':
      player = PlayerUtils.getPlayerByName(newState, action.value.playerName)
      player.hand = action.value.value
      break
    }

    return newState
  }

}