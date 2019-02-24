import Card from '../Card/Card'
import stompClient from '../WebSocket'

export default class ClientEventsReducer {

  static getEvents() {
    return ['@@INIT', 'PLAYER_CARD_CLICK']
  }

  static reduceEvent(state, newState, action) {
    switch (action.type) {
      case '@@INIT':
        return {}

      case 'PLAYER_CARD_CLICK':
        if (state.turn.currentPhaseActivePlayer === state.player.name) {
          const cardId = Card.extractCardId(action.cardId)
          Card.findCardById(state.player.hand, cardId)
          stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardId: cardId})
        }
        return newState
    }
  }

}