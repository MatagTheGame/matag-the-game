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
          const cardInstance = Card.findCardInstanceById(state.player.hand, cardId)
          if (cardInstance.card.types.find(type => type === 'LAND')) {
            stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardId: cardId})
          }
        }
        return newState
    }
  }

}