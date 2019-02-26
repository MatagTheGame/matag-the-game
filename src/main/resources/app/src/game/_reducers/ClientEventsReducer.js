import stompClient from '../WebSocket'
import CardComponent from '../Card/CardComponent'

export default class ClientEventsReducer {

  static getEvents() {
    return ['@@INIT', 'PLAYER_HAND_CARD_CLICK', 'CONTINUE_CLICK']
  }

  static reduceEvent(state, newState, action) {
    switch (action.type) {
      case '@@INIT':
        return {}

      case 'PLAYER_HAND_CARD_CLICK':
        if (state.turn.currentPhaseActivePlayer === state.player.name) {
          const cardId = CardComponent.extractCardId(action.cardId)
          const cardInstance = CardComponent.findCardInstanceById(state.player.hand, cardId)
          if (state.turn.triggeredAction === 'DISCARD_A_CARD') {
            stompClient.sendEvent('turn', {action: 'RESOLVE', type: 'DISCARD_A_CARD', cardId: cardId})

          } else if (cardInstance.card.types.find(type => type === 'LAND')) {
            stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardId: cardId})
          }
        }
        return newState

      case 'CONTINUE_CLICK':
        stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
        return newState
    }
  }

}