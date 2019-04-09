import stompClient from '../WebSocket'
import Phase from '../Turn/Phase'
import CostUtils from '../Card/CostUtils'
import CardUtils from '../Card/CardUtils'
import CardSearch from '../Card/CardSearch'

export default class ClientEventsReducer {

  static getEvents() {
    return ['@@INIT', 'PLAYER_HAND_CARD_CLICK', 'PLAYER_BATTLEFIELD_CARD_CLICK', 'OPPONENT_BATTLEFIELD_CARD_CLICK', 'CONTINUE_CLICK', 'MAXIMIZE_MINIMIZE_CARD']
  }

  static reduceEvent(newState, action) {
    switch (action.type) {
      case '@@INIT':
        return {}

      case 'MAXIMIZE_MINIMIZE_CARD':
        newState.maximizedCard = action.value.cardImage
        break;

      case 'PLAYER_HAND_CARD_CLICK':
        if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
          const cardId = action.cardId
          const cardInstance = CardSearch.cards(newState.player.hand).withId(cardId)
          if (newState.turn.triggeredAction === 'DISCARD_A_CARD') {
            stompClient.sendEvent('turn', {action: 'RESOLVE', triggeredAction: 'DISCARD_A_CARD', cardIds: [cardId]})
          }

          if (Phase.isMainPhase(newState.turn.currentPhase)) {
            if (CardUtils.isOfType(cardInstance, 'LAND')) {
              stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardIds: [cardId]})

            } else {
              const currentMana = CostUtils.currentMana(newState.player.battlefield)
              const currentManaIds = CostUtils.currentManaCardIds(newState.player.battlefield)
              if (CostUtils.isCastingCostFulfilled(cardInstance.card, currentMana)) {
                stompClient.sendEvent('turn', {action: 'CAST', cardIds: [cardId], tappingLandIds: currentManaIds})
              }
            }
          }
        }
        break

      case 'PLAYER_BATTLEFIELD_CARD_CLICK':
        if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
          const cardInstance = CardSearch.cards(newState.player.battlefield).withId(action.cardId)

          if (newState.turn.currentPhase === 'DA') {
            if (CardUtils.isOfType(cardInstance, 'CREATURE') && !CardUtils.hasSummoningSickness(cardInstance)) {
              CardUtils.toggleFrontendTapped(cardInstance)
            }

          } else if (newState.turn.currentPhase === 'DB') {
            if (CardUtils.isOfType(cardInstance, 'CREATURE')) {
              const blockingCard = CardSearch.cards(newState.opponent.battlefield).attacking()[newState.turn.blockingCardPosition]
              CardUtils.toggleFrontendBlocking(cardInstance, blockingCard.id)
            }

          } else {
            if (CardUtils.isOfType(cardInstance, 'LAND')) {
              CardUtils.toggleFrontendTapped(cardInstance)
            }
          }
        }
        break

      case 'OPPONENT_BATTLEFIELD_CARD_CLICK':
        if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
          const cardInstance = CardSearch.cards(newState.opponent.battlefield).withId(action.cardId)

          if (newState.turn.currentPhase === 'DB') {
            if (CardUtils.isOfType(cardInstance, 'CREATURE')) {
              newState.turn.blockingCardPosition = CardSearch.cards(newState.opponent.battlefield).attacking().indexOf(cardInstance)
            }
          }
        }
        break

      case 'CONTINUE_CLICK':
        if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
          if (newState.turn.currentPhase === 'DA') {
            const attackingCreaturesIds = CardSearch.cards(newState.player.battlefield)
              .frontEndTapped()
              .ofType('CREATURE')
              .map(cardInstance => cardInstance.id)

            if (attackingCreaturesIds.length > 0) {
              stompClient.sendEvent('turn', {action: 'DECLARE_ATTACKERS', cardIds: attackingCreaturesIds})
              break
            }

          } else if (newState.turn.currentPhase === 'DB') {
            const blockingCreatures = CardSearch.cards(newState.player.battlefield).frontEndBlocking()

            if (blockingCreatures.length > 0) {
              stompClient.sendEvent('turn', {
                action: 'DECLARE_BLOCKERS',
                targetsIdsForCardIds: CardUtils.blockingCreaturesToTargetIdsEvent(blockingCreatures)
              })
              break
            }

          }

          CardSearch.cards(newState.player.battlefield)
            .frontEndTapped()
            .forEach((cardInstance) => CardUtils.untap(cardInstance))

          stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
        }

        break
    }

    return newState
  }

}