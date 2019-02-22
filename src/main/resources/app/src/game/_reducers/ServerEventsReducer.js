import Phase from '../Turn/Phase'
import stompClient from '../WebSocket'

export default class ServerEventsReducer {

  static getEvents() {
    return ['INIT_WAITING_OPPONENT', 'OPPONENT_JOINED', 'INIT_PLAYER', 'INIT_OPPONENT', 'UPDATE_TURN']
  }

  static reduceEvent(state, newState, action) {
    switch (action.type) {
      case 'INIT_WAITING_OPPONENT':
        return Object.assign(newState, {message: 'Waiting for opponent...'})

      case 'OPPONENT_JOINED':
        return Object.assign(newState, {message: undefined})

      case 'INIT_PLAYER':
        return Object.assign(newState, {player: action.value})

      case 'INIT_OPPONENT':
        return Object.assign(newState, {opponent: action.value})

      case 'UPDATE_TURN':
        if (action.value.currentPhaseActivePlayer === state.player.name && !Phase.isMainPhase(action.value.currentPhase)) {
          stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
        }

        return Object.assign(newState, {turn: action.value})
    }
  }

}