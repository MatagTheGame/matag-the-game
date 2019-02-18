import stompClient from '../WebSocket'
import Phase from '../Turn/Phase'

export default (state, action) => {
  const newState = Object.assign({}, state)

    switch (action.type) {
    case 'INIT_WAITING_OPPONENT':
      return Object.assign(newState, {message: 'Waiting for opponent...'})

    case 'OPPONENT_JOINED':
      return Object.assign(newState, {message: undefined})

    case 'INIT_PLAYER':
      return Object.assign(newState, {player: action.value})

    case 'INIT_OPPONENT':
      return Object.assign(newState, {opponent: action.value})

    case 'INIT_PHASES_CONFIG':
      return Object.assign(newState, {turn: action.value})

    case 'UPDATE_TURN':
      if (action.value.currentPhaseActivePlayer === state.player.name && Phase.isPhaseEnabled(state.turn.phasesConfig, action.value.currentPhase)) {
        stompClient.sendEvent('turn', {action: 'PASS_PRIORITY'})
      }

      return Object.assign(newState, {turn: {phasesConfig: state.turn.phasesConfig, status: action.value}})

    case '@@INIT':
      return {}

    default:
      throw 'Unknown action type ' + action.type
  }
}