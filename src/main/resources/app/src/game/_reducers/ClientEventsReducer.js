export default class ClientEventsReducer {

  static getEvents() {
    return ['@@INIT', 'PLAYER_CARD_CLICK']
  }

  static reduceEvent(state, newState, action) {
    switch (action.type) {
      case '@@INIT':
        return {}

      case 'PLAYER_CARD_CLICK':
        console.log(action)
        return newState
    }
  }

}