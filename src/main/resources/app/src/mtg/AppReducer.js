export default (state, action) => {
  switch (action.type) {
    case 'INIT_WAITING_OPPONENT':
      return Object.assign(state, {message: 'Waiting for opponent...'})

    default:
      return {
        message: 'ciao',
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