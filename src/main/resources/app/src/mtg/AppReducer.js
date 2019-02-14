export default (state, action) => {
  const newState = Object.assign({}, state)
  switch (action.type) {
    case 'INIT_WAITING_OPPONENT':
      return Object.assign(newState, {message: 'Waiting for opponent...'})

    default:
      return {
        player: {
          hand: ['forest', 'mountain', 'alpha_tyrranax'],
          deck: ['card'],
          battlefield: ['forest', 'forest', 'mountain', 'mountain', 'mountain']
        },
        opponent: {
          hand: ['card', 'card', 'card', 'card'],
          deck: ['card'],
          battlefield: ['forest', 'forest', 'mountain']
        }
      }
  }
}