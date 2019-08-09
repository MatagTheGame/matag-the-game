import stompClient from '../WebSocket'
import Phase from '../Turn/Phase'
import CostUtils from '../Card/CostUtils'
import CardUtils from '../Card/CardUtils'
import CardSearch from '../Card/CardSearch'
import StackUtils from '../Stack/StackUtils'
import PlayerUtils from '../PlayerInfo/PlayerUtils'

export default class ClientEventsReducer {

  static getEvents() {
    return ['@@INIT', 'PLAYER_HAND_CARD_CLICK', 'PLAYER_BATTLEFIELD_CARD_CLICK', 'OPPONENT_BATTLEFIELD_CARD_CLICK', 'CONTINUE_CLICK',
      'PLAYER_CLICK', 'MAXIMIZE_MINIMIZE_CARD']
  }

  static reduceEvent(newState, action) {
    switch (action.type) {
    case 'MAXIMIZE_MINIMIZE_CARD':
      newState.maximizedCard = action.value.cardImage
      break

    case 'PLAYER_HAND_CARD_CLICK':
      if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
        const cardId = action.cardId
        const cardInstance = CardSearch.cards(newState.player.hand).withId(cardId)
        if (newState.turn.triggeredNonStackAction === 'DISCARD_A_CARD') {
          stompClient.sendEvent('turn', {action: 'RESOLVE', triggeredNonStackAction: 'DISCARD_A_CARD', cardIds: [cardId]})
        }

        if (Phase.isMainPhase(newState.turn.currentPhase) || cardInstance.card.instantSpeed) {
          // TODO Antonio: this is very similar to the one for battlefield click
          if (CardUtils.isOfType(cardInstance, 'LAND')) {
            stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardIds: [cardId]})

          } else {
            const currentMana = CostUtils.currentMana(newState.player.battlefield)
            const ability = CardUtils.getCastAbility(cardInstance)
            if (CostUtils.isCastingCostFulfilled(cardInstance.card, currentMana)) {
              if (CardUtils.needsTargets(cardInstance, 'CAST')) {
                PlayerUtils.handleSelectTargets(newState, cardInstance, ability)
              } else {
                PlayerUtils.cast(newState, cardId, {}, PlayerUtils.getAbilityToBePlayed(ability))
              }
            }
          }
        }
      }
      break

    case 'PLAYER_BATTLEFIELD_CARD_CLICK':
      if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
        const cardInstance = CardSearch.cards(newState.player.battlefield).withId(action.cardId)
        const playedAbility = CardUtils.getAbilityForTriggerType(cardInstance, 'ACTIVATED_ABILITY')

        if (newState.turn.currentPhase === 'DA' && PlayerUtils.isCurrentPlayerTurn(newState)) {
          const canAttackResult = CardUtils.canAttack(cardInstance)
          if (canAttackResult === true) {
            CardUtils.toggleFrontendAttacking(cardInstance)
          } else {
            newState.message = {text: canAttackResult, closable: true}
          }

        } else if (newState.turn.currentPhase === 'DB') {
          const blockedCard = CardSearch.cards(newState.opponent.battlefield).attacking()[newState.turn.blockingCardPosition]
          const canBlockResult = CardUtils.canBlock(cardInstance, blockedCard)
          if (canBlockResult === true) {
            CardUtils.toggleFrontendBlocking(cardInstance, blockedCard.id)
          } else {
            newState.message = {text: canBlockResult, closable: true}
          }

        } else if (newState.turn.cardIdSelectedToBePlayed) {
          PlayerUtils.handleSelectedTarget(newState, cardInstance)

        } else {
          // TODO Antonio: this is very similar to the one for hand click
          if (CardUtils.isOfType(cardInstance, 'LAND')) {
            CardUtils.toggleFrontendTapped(cardInstance)

          } else {
            if (playedAbility) {
              const currentMana = CostUtils.currentMana(newState.player.battlefield)
              if (CostUtils.isAbilityCostFulfilled(playedAbility, currentMana)) {
                if (CardUtils.needsTargets(cardInstance, 'ACTIVATED_ABILITY')) {
                  PlayerUtils.handleSelectTargets(newState, cardInstance, playedAbility)
                }
              }
            }
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

        } else if (newState.turn.cardIdSelectedToBePlayed) {
          PlayerUtils.handleSelectedTarget(newState, cardInstance)
        }
      }
      break

    case 'CONTINUE_CLICK':
      if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
        if (newState.turn.currentPhase === 'DA') {
          const attackingCreaturesIds = CardSearch.cards(newState.player.battlefield)
            .frontEndAttacking()
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

        if (!StackUtils.isStackEmpty(newState)) {
          stompClient.sendEvent('turn', {action: 'RESOLVE'})

        } else if (CardSearch.cards(newState.player.battlefield).frontEndTapped().isNotEmpty()) {
          CardSearch.cards(newState.player.battlefield)
            .frontEndTapped()
            .forEach((cardInstance) => CardUtils.untap(cardInstance))

        } else {
          stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
        }
      }

      break

    case 'PLAYER_CLICK':
      if (newState.turn.cardIdSelectedToBePlayed) {
        PlayerUtils.handleSelectedTarget(newState, action.playerName)
      }
      break
    }

    return newState
  }

}