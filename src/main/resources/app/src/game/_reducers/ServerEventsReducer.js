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
        return Object.assign(newState, {message: action.value})

      case 'INIT_WAITING_OPPONENT':
        return Object.assign(newState, {message: {text: 'Waiting for opponent...', closable: false}})

      case 'OPPONENT_JOINED':
        return Object.assign(newState, {message: {text: undefined}})

      case 'INIT_PLAYER':
        return Object.assign(newState, {player: action.value})

      case 'INIT_OPPONENT':
        return Object.assign(newState, {opponent: action.value})

      case 'UPDATE_TURN':
        Object.assign(newState, {turn: action.value})

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

        return Object.assign(newState, {statusMessage: StatusMessageComponent.getStatusMessageForUser(newState)})

      case 'UPDATE_ACTIVE_PLAYER_BATTLEFIELD':
        activePlayer = PlayerUtils.getActivePlayer(state)
        activePlayer.battlefield = action.value.cards
        return PlayerUtils.updateActivePlayer(newState, activePlayer)

      case 'UPDATE_ACTIVE_PLAYER_HAND':
        activePlayer = PlayerUtils.getActivePlayer(state)
        activePlayer.hand = action.value
        return PlayerUtils.updateActivePlayer(newState, activePlayer)

      case 'UPDATE_ACTIVE_PLAYER_GRAVEYARD':
        activePlayer = PlayerUtils.getActivePlayer(state)
        activePlayer.graveyard = action.value.cards
        return PlayerUtils.updateActivePlayer(newState, activePlayer)
    }
  }

}