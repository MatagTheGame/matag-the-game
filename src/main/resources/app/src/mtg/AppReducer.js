import stompClient from './WebSocket'

const isPhaseEnabled = (phasesConfig, phaseName) => {
  const phaseConfig = phasesConfig.find((phaseConfig) => phaseConfig.name === phaseName)
  if (phaseConfig) {
    return phaseConfig.status === 'DISABLED'
  }
  return false
}

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
      if (action.value.currentPhaseActivePlayer === state.player.name && isPhaseEnabled(state.turn.phasesConfig, action.value.currentPhase)) {
        stompClient.send('/api/turn', {}, JSON.stringify({action: 'PASS_PRIORITY'}))
      }

      return Object.assign(newState, {turn: {phasesConfig: state.turn.phasesConfig, status: action.value}})

    case '@@INIT':
      return {}

    default:
      throw 'Unknown action type ' + action.type
  }
}