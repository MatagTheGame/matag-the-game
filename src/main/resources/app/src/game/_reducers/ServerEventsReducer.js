import stompClient from '../WebSocket'
import Phase from '../Turn/Phase'
import PlayerUtils from '../PlayerInfo/PlayerUtils'
import StatusMessageComponent from '../UserAction/StatusMessageComponent'

export default class ServerEventsReducer {
  static getEvents() {
    return ['MESSAGE', 'INIT_WAITING_OPPONENT', 'OPPONENT_JOINED', 'INIT_PLAYER', 'INIT_OPPONENT', 'UPDATE_TURN', 'UPDATE_ACTIVE_PLAYER_BATTLEFIELD', 'UPDATE_ACTIVE_PLAYER_HAND', 'UPDATE_ACTIVE_PLAYER_GRAVEYARD']
  }

  static reduceEvent(state, newState, action) {
    let activePlayer
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

        if (PlayerUtils.isCurrentPlayerActive(newState)) {
          if (PlayerUtils.isCurrentPlayerTurn(newState)) {
            if (!Phase.isMainPhase(newState.turn.currentPhase)) {
              if (!PlayerUtils.canActivePlayerPerformAnyAction(newState)) {
                stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
              }
            }
          } else {
            if (!PlayerUtils.canActivePlayerPerformAnyAction(newState)) {
              stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
            }
          }
        }

        newState.statusMessage = StatusMessageComponent.getStatusMessageForUser(newState)
        break

      case 'UPDATE_ACTIVE_PLAYER_BATTLEFIELD':
        activePlayer = PlayerUtils.getActivePlayer(newState)
        activePlayer.battlefield = action.value
        break

      case 'UPDATE_ACTIVE_PLAYER_HAND':
        activePlayer = PlayerUtils.getActivePlayer(newState)
        activePlayer.hand = action.value
        break

      case 'UPDATE_ACTIVE_PLAYER_GRAVEYARD':
        activePlayer = PlayerUtils.getActivePlayer(newState)
        activePlayer.graveyard = action.value
        break
    }

    return newState
  }

}