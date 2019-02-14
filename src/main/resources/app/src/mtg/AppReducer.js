const initialState = {
  player: {
    hand: {
      cards: []
    },
    library: {
      cards: []
    },
    battlefield: {
      cards: []
    }
  },
  opponent: {
    hand: [],
    deck: [],
    battlefield: []
  }
}

export default (state, action) => {
  const newState = Object.assign({}, state)

    switch (action.type) {
    case 'INIT_WAITING_OPPONENT':
      return Object.assign(newState, {message: 'Waiting for opponent...'})

    case 'INIT_PLAYER':
      return Object.assign(newState, {player: action.value})

    case 'INIT_COMPLETED':
      return Object.assign(newState, {message: undefined})

    case '@@INIT':
      console.log('@@INIT action type')
      return initialState

    default:
      throw 'Unknown action type ' + action.type
  }
}