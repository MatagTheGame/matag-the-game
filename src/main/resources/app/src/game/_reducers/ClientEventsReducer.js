import stompClient from '../WebSocket'
import Phase from '../Turn/Phase'
import CostUtils from '../Card/CostUtils'
import CardUtils from '../Card/CardUtils'

export default class ClientEventsReducer {

  static getEvents() {
    return ['@@INIT', 'PLAYER_HAND_CARD_CLICK', 'PLAYER_BATTLEFIELD_CARD_CLICK', 'CONTINUE_CLICK', 'MAXIMIZE_MINIMIZE_CARD']
  }

  static reduceEvent(newState, action) {
    switch (action.type) {
      case '@@INIT':
        return {}

      case 'MAXIMIZE_MINIMIZE_CARD':
        console.log('MAXIMIZE_MINIMIZE_CARD: ', action)
        newState.maximizedCard = action.value.cardImage
        break;

      case 'PLAYER_HAND_CARD_CLICK':
        if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
          const cardId = CardUtils.extractCardId(action.cardId)
          const cardInstance = CardUtils.findCardInstanceById(newState.player.hand, cardId)
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
          const cardId = CardUtils.extractCardId(action.cardId)
          const cardInstance = CardUtils.findCardInstanceById(newState.player.battlefield, cardId)
          if (cardInstance.card.types.includes('LAND')) {
            if (CardUtils.isUntapped(cardInstance)) {
              CardUtils.frontendTap(cardInstance)
            } else if (CardUtils.isFrontendTapped(cardInstance)) {
              CardUtils.untap(cardInstance)
            }
          }
        }
        break

      case 'CONTINUE_CLICK':
        CardUtils.untapAllFrontendTappedCards(newState.player.battlefield)
        stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
        break
    }

    return newState
  }

}