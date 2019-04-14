import stompClient from '../WebSocket'
import PlayerUtils from '../PlayerInfo/PlayerUtils'

export default class ServerEventsReducer {
  static getEvents() {
    return ['MESSAGE', 'INIT_WAITING_OPPONENT', 'OPPONENT_JOINED', 'INIT_PLAYER', 'INIT_OPPONENT', 'UPDATE_TURN',
      'UPDATE_ACTIVE_PLAYER_BATTLEFIELD', 'UPDATE_INACTIVE_PLAYER_BATTLEFIELD', 'UPDATE_ACTIVE_PLAYER_HAND', 'UPDATE_INACTIVE_PLAYER_HAND',
      'UPDATE_ACTIVE_PLAYER_GRAVEYARD', 'UPDATE_INACTIVE_PLAYER_GRAVEYARD', 'UPDATE_PLAYER_LIFE', 'UPDATE_STACK',
      'UPDATE_ACTIVE_PLAYER_LIBRARY_SIZE', 'UPDATE_INACTIVE_PLAYER_LIBRARY_SIZE']
  }

  static reduceEvent(newState, action) {
    let currentPlayer, nonCurrentPlayer, player
    switch (action.type) {
      case 'MESSAGE':
        newState.message = action.value
        break

      case 'INIT_WAITING_OPPONENT':
        newState.message = {text: 'Waiting for opponent...', closable: false}
        break

      case 'OPPONENT_JOINED':
        newState.message.text = undefined
        break

      case 'INIT_PLAYER':
        newState.player = action.value
        break

      case 'INIT_OPPONENT':
        newState.opponent = action.value
        break

      case 'UPDATE_TURN':
        newState.turn = action.value
        newState.turn.blockingCardPosition = 0

        if (!newState.turn.winner && !PlayerUtils.canPlayerPerformAnyAction(newState)) {
          if (PlayerUtils.isCurrentPlayerActive(newState)) {
            stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
          }
        } else if (newState.turn.winner) {
          newState.message = {text: newState.turn.winner + ' Win!', closable: true}
        }

        break

      case 'UPDATE_ACTIVE_PLAYER_BATTLEFIELD':
        currentPlayer = PlayerUtils.getCurrentPlayer(newState)
        currentPlayer.battlefield = action.value
        break

      case 'UPDATE_INACTIVE_PLAYER_BATTLEFIELD':
        nonCurrentPlayer = PlayerUtils.getNonCurrentPlayer(newState)
        nonCurrentPlayer.battlefield = action.value
        break

      case 'UPDATE_ACTIVE_PLAYER_HAND':
        currentPlayer = PlayerUtils.getCurrentPlayer(newState)
        currentPlayer.hand = action.value
        break

      case 'UPDATE_INACTIVE_PLAYER_HAND':
        nonCurrentPlayer = PlayerUtils.getNonCurrentPlayer(newState)
        nonCurrentPlayer.hand = action.value
        break

      case 'UPDATE_ACTIVE_PLAYER_GRAVEYARD':
        currentPlayer = PlayerUtils.getCurrentPlayer(newState)
        currentPlayer.graveyard = action.value
        break

      case 'UPDATE_INACTIVE_PLAYER_GRAVEYARD':
        nonCurrentPlayer = PlayerUtils.getNonCurrentPlayer(newState)
        nonCurrentPlayer.graveyard = action.value
        break

      case 'UPDATE_PLAYER_LIFE':
        player = PlayerUtils.getPlayerByName(newState, action.value.playerName)
        player.life = action.value.life
        break

      case 'UPDATE_ACTIVE_PLAYER_LIBRARY_SIZE':
        currentPlayer = PlayerUtils.getCurrentPlayer(newState)
        currentPlayer.librarySize = action.value
        break

      case 'UPDATE_INACTIVE_PLAYER_LIBRARY_SIZE':
        nonCurrentPlayer = PlayerUtils.getNonCurrentPlayer(newState)
        nonCurrentPlayer.librarySize = action.value
        break

      case 'UPDATE_STACK':
        newState.stack = action.value
        break
    }

    return newState
  }

}