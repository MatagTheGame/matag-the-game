import stompClient from '../WebSocket'
import CardComponent from '../Card/CardComponent'

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
          const cardId = CardComponent.extractCardId(action.cardId)
          const cardInstance = CardComponent.findCardInstanceById(state.player.hand, cardId)
          if (cardInstance.card.types.find(type => type === 'LAND')) {
            stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardId: cardId})
          }
        }
        return newState
    }
  }

}