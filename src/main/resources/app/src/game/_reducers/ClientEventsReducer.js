import stompClient from '../WebSocket'
import CardComponent from '../Card/CardComponent'

export default class ClientEventsReducer {

  static getEvents() {
    return ['@@INIT', 'PLAYER_HAND_CARD_CLICK', 'PLAYER_BATTLEFIELD_CARD_CLICK', 'CONTINUE_CLICK']
  }

  static reduceEvent(state, newState, action) {
    switch (action.type) {
      case '@@INIT':
        return {}

      case 'PLAYER_HAND_CARD_CLICK':
        if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
          const cardId = CardComponent.extractCardId(action.cardId)
          const cardInstance = CardComponent.findCardInstanceById(newState.player.hand, cardId)
          if (newState.turn.triggeredAction === 'DISCARD_A_CARD') {
            stompClient.sendEvent('turn', {action: 'RESOLVE', triggeredAction: 'DISCARD_A_CARD', cardId: cardId})

          } else if (cardInstance.card.types.find(type => type === 'LAND')) {
            stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardId: cardId})
          }
        }
        break

      case 'PLAYER_BATTLEFIELD_CARD_CLICK':
        if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
          const cardId = CardComponent.extractCardId(action.cardId)
          const cardInstance = CardComponent.findCardInstanceById(newState.player.battlefield, cardId)
          if (cardInstance.card.types.find(type => type === 'LAND')) {
            if (cardInstance.modifiers.tapped === 'FRONTEND_TAPPED') {
              cardInstance.modifiers.tapped = undefined
            } else {
              cardInstance.modifiers.tapped = 'FRONTEND_TAPPED'
            }
          }
        }
        break

      case 'CONTINUE_CLICK':
        stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
        break
    }

    return newState
  }

}