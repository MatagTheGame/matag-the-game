import stompClient from '../WebSocket'
import Phase from '../Turn/Phase'
import Player from '../PlayerInfo/Player'

export default class ServerEventsReducer {

  static getEvents() {
    return ['INIT_WAITING_OPPONENT', 'OPPONENT_JOINED', 'INIT_PLAYER', 'INIT_OPPONENT', 'UPDATE_TURN', 'UPDATE_ACTIVE_PLAYER_BATTLEFIELD']
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

      case 'UPDATE_ACTIVE_PLAYER_BATTLEFIELD':
        const activePlayer = Player.getActivePlayer(state)

        const updatedActivePlayer = Object.assign({}, activePlayer)
        updatedActivePlayer.battlefield = action.value.cards

        return Player.updateActivePlayer(newState, updatedActivePlayer)
    }
  }

}