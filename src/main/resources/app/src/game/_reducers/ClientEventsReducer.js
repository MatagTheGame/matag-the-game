import stompClient from '../WebSocket'
import CardComponent from '../Card/CardComponent'
import Phase from '../Turn/Phase'
import CostUtils from '../Card/CostUtils'

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
          }

          if (Phase.isMainPhase(newState.turn.currentPhase)) {
            if (cardInstance.card.types.find(type => type === 'LAND')) {
              stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardId: cardId})

            } else {
              const currentMana = CostUtils.currentMana(newState.player.battlefield)
              const currentManaIds = CostUtils.currentManaCardIds(newState.player.battlefield)
              if (CostUtils.isCastingCostFulfilled(cardInstance.card, currentMana)) {
                stompClient.sendEvent('turn', {action: 'CAST', cardId: cardId, tappingLandIds: currentManaIds})
              }
            }
          }
        }
        break

      case 'PLAYER_BATTLEFIELD_CARD_CLICK':
        if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
          const cardId = CardComponent.extractCardId(action.cardId)
          const cardInstance = CardComponent.findCardInstanceById(newState.player.battlefield, cardId)
          if (cardInstance.card.types.find(type => type === 'LAND')) {
            CardComponent.toggleFrontendTapped(cardInstance)
          }
        }
        break

      case 'CONTINUE_CLICK':
        stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
        CardComponent.untapAllFrontendTappedCards(newState.player.battlefield)
        break
    }

    return newState
  }

}