const initialState = {
  player: {
    hand: [],
    deck: [],
    battlefield: []
  },
  opponent: {
    hand: [],
    deck: [],
    battlefield: []
  }
}

export default (state, action) => {
  const newState = Object.assign({}, state)

  if (!action.type) {
    return initialState
  }

  switch (action.type) {
    case 'INIT_WAITING_OPPONENT':
      return Object.assign(newState, {message: 'Waiting for opponent...'})

    case 'INIT_PLAYER':
      return Object.assign(newState, {player: action.value})

    case 'INIT_COMPLETED':
      return Object.assign(newState, {message: undefined})

    default:
      throw 'Unknown action ' + action.type
  }
}