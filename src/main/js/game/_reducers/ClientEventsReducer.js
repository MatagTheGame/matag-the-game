import CardSearch from 'game/Card/CardSearch'
import CardUtils from 'game/Card/CardUtils'
import CostUtils from 'game/Card/CostUtils'
import PlayerUtils from 'game/PlayerInfo/PlayerUtils'
import StackUtils from 'game/Stack/StackUtils'
import Phase from 'game/Turn/Phase'
import TurnUtils from 'game/Turn/TurnUtils'
import UserInterfaceUtils from 'game/UserInterface/UserInterfaceUtils'
import stompClient from 'game/WebSocket'

export default class ClientEventsReducer {

  static getEvents() {
    return ['@@INIT', 'PLAYER_HAND_CARD_CLICK', 'PLAYER_BATTLEFIELD_CARD_CLICK', 'OPPONENT_BATTLEFIELD_CARD_CLICK', 'STACK_ELEMENT_CLICK',
      'VISIBLE_LIBRARY_CARD_CLICK', 'CONTINUE_CLICK', 'PLAYER_CLICK', 'MAXIMIZE_MINIMIZE_CARD', 'CLOSE_USER_INPUTS_CLICK',
      'PLAY_ABILITIES_CLICK', 'OPEN_HELP_PAGE', 'CLOSE_HELP_PAGE']
  }

  static reduceEvent(newState, action) {
    switch (action.type) {
    case 'MAXIMIZE_MINIMIZE_CARD':
      newState.maximizedCard = action.value.cardImage
      break

    case 'OPEN_HELP_PAGE':
      newState.helpOpen = true
      break

    case 'CLOSE_HELP_PAGE':
      newState.helpOpen = false
      break

    case 'CLOSE_USER_INPUTS_CLICK':
      UserInterfaceUtils.unsetPlayableAbilities(newState)
      break

    case 'PLAY_ABILITIES_CLICK':
      const cardInstance = CardSearch.cards(newState.player.battlefield).withId(action.cardId)

      const playedAbility = CardUtils.getAbilitiesForTriggerTypes(cardInstance, ['ACTIVATED_ABILITY', 'MANA_ABILITY'])[action.index]
      if (playedAbility.trigger.type === 'MANA_ABILITY') {
        CardUtils.activateManaAbility(newState, cardInstance, action.index)

      } else if (playedAbility.trigger.type === 'ACTIVATED_ABILITY') {
        PlayerUtils.castOrHandleTargets(newState, cardInstance, playedAbility)
      }

      break

    case 'PLAYER_HAND_CARD_CLICK':
      if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
        const cardId = action.cardId
        const cardInstance = CardSearch.cards(newState.player.hand).withId(cardId)
        if (TurnUtils.inputRequiredActionIs(newState, 'DISCARD_A_CARD')) {
          const cardsToDiscard = parseInt(newState.turn.inputRequiredActionParameter)
          if (TurnUtils.getTargetsIds(newState).length < cardsToDiscard) {
            TurnUtils.selectDifferentTargets(newState, cardInstance)
            if (TurnUtils.getTargetsIds(newState).length === cardsToDiscard) {
              stompClient.sendEvent('turn', {action: 'RESOLVE', inputRequiredAction: 'DISCARD_A_CARD', cardIds: TurnUtils.getTargetsIds(newState)})
            }
          }
        }

        if (Phase.isMainPhase(newState.turn.currentPhase) || cardInstance.instantSpeed) {
          if (CardUtils.isOfType(cardInstance, 'LAND')) {
            stompClient.sendEvent('turn', {action: 'PLAY_LAND', cardIds: [cardId]})

          } else {
            const castAbilities = CardUtils.getAbilitiesForTriggerType(cardInstance, 'CAST')
            // TODO Antonio: this is very similar to the one for battlefield click
            const currentTappedMana = CostUtils.getMana(newState)
            if (CostUtils.isCastingCostFulfilled(cardInstance, currentTappedMana)) {
              if (CardUtils.needsTargets(newState, castAbilities)) {
                PlayerUtils.handleSelectTargets(newState, cardInstance, castAbilities)
              } else {
                PlayerUtils.cast(newState, cardId, {})
              }
            }
          }
        }
      }
      break

    case 'PLAYER_BATTLEFIELD_CARD_CLICK':
      if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
        const cardId = action.cardId
        const cardInstance = CardSearch.cards(newState.player.battlefield).withId(cardId)

        if (newState.turn.currentPhase === 'DA' && TurnUtils.inputRequiredActionIs(newState, 'DECLARE_ATTACKERS')) {
          const canAttackResult = CardUtils.canAttack(cardInstance)
          if (canAttackResult === true) {
            CardUtils.toggleFrontendAttacking(cardInstance)
          } else {
            UserInterfaceUtils.setMessage(newState, canAttackResult)
          }

        } else if (newState.turn.currentPhase === 'DB' && TurnUtils.inputRequiredActionIs(newState, 'DECLARE_BLOCKERS')) {
          const blockedCard = CardSearch.cards(newState.opponent.battlefield).attacking()[newState.turn.blockingCardPosition]
          const canBlockResult = CardUtils.canBlock(cardInstance, blockedCard)
          if (canBlockResult === true) {
            CardUtils.toggleFrontendBlocking(cardInstance, blockedCard.id)
          } else {
            UserInterfaceUtils.setMessage(newState, canBlockResult)
          }

        } else if (PlayerUtils.shouldHandleTargets(newState)) {
          PlayerUtils.handleSelectedTarget(newState, cardInstance)

        } else {
          const possibleAbilities = CardUtils.getAbilitiesForTriggerTypes(cardInstance, ['MANA_ABILITY', 'ACTIVATED_ABILITY'])

          // FIXME this is wrong. It should be still possible to play an ability of a tapped card.
          //  And if a card is tapped in the dropdown list should not appear all the abilities that require tapping.
          if (!CardUtils.isFrontendTapped(cardInstance) && possibleAbilities.length > 1) {
            UserInterfaceUtils.setPlayableAbilities(newState, cardId, possibleAbilities, action.position)

          } else if (CardUtils.getAbilitiesForTriggerType(cardInstance, 'MANA_ABILITY').length > 0) {
              CardUtils.activateManaAbility(newState, cardInstance)

          } else {
            const playedAbility = CardUtils.getAbilitiesForTriggerType(cardInstance, 'ACTIVATED_ABILITY')[0]
            if (playedAbility) {
              PlayerUtils.castOrHandleTargets(newState, cardInstance, playedAbility)
            }
          }
        }
      }
      break

    case 'OPPONENT_BATTLEFIELD_CARD_CLICK':
      if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
        const cardInstance = CardSearch.cards(newState.opponent.battlefield).withId(action.cardId)

        if (newState.turn.currentPhase === 'DB' && TurnUtils.inputRequiredActionIs(newState, 'DECLARE_BLOCKERS')) {
          if (CardUtils.isOfType(cardInstance, 'CREATURE')) {
            newState.turn.blockingCardPosition = CardSearch.cards(newState.opponent.battlefield).attacking().indexOf(cardInstance)
          }

        } else if (PlayerUtils.shouldHandleTargets(newState)) {
          PlayerUtils.handleSelectedTarget(newState, cardInstance)
        }
      }
      break

    case 'STACK_ELEMENT_CLICK':
      if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
        const cardInstance = CardSearch.cards(newState.stack).withId(action.cardId)
        if (PlayerUtils.shouldHandleTargets(newState)) {
          PlayerUtils.handleSelectedTarget(newState, cardInstance)
        }
      }
      break

    case 'VISIBLE_LIBRARY_CARD_CLICK':
      if (TurnUtils.inputRequiredActionIs(newState, 'SCRY')) {
        const cardsToScry = parseInt(newState.turn.inputRequiredActionParameter)
        console.log('SCRY ' + cardsToScry)
      }
      break

    case 'CONTINUE_CLICK':
      if (newState.turn.currentPhaseActivePlayer === newState.player.name) {
        if (newState.turn.currentPhase === 'DA' && TurnUtils.inputRequiredActionIs(newState, 'DECLARE_ATTACKERS')) {
          const attackingCreaturesIds = CardSearch.cards(newState.player.battlefield)
            .frontEndAttacking()
            .ofType('CREATURE')
            .map(cardInstance => cardInstance.id)
          stompClient.sendEvent('turn', {action: 'DECLARE_ATTACKERS', cardIds: attackingCreaturesIds})
          break

        } else if (newState.turn.currentPhase === 'DB' && TurnUtils.inputRequiredActionIs(newState, 'DECLARE_BLOCKERS')) {
          const blockingCreatures = CardSearch.cards(newState.player.battlefield).frontEndBlocking()
          stompClient.sendEvent('turn', {
            action: 'DECLARE_BLOCKERS',
            targetsIdsForCardIds: CardUtils.blockingCreaturesToTargetIdsEvent(blockingCreatures)
          })
          break
        }

        if (!StackUtils.isStackEmpty(newState)) {
          stompClient.sendEvent('turn', {action: 'RESOLVE'})

        } else if (TurnUtils.getCardIdSelectedToBePlayed(newState)) {
          PlayerUtils.castSelectedCard(newState)

        } else if (CardSearch.cards(newState.player.battlefield).frontEndTapped().isNotEmpty()) {
          CardSearch.cards(newState.player.battlefield)
            .frontEndTapped()
            .forEach(CardUtils.untap)

        } else {
          stompClient.sendEvent('turn', {action: 'CONTINUE_TURN'})
        }
      }

      break

    case 'PLAYER_CLICK':
      if (PlayerUtils.shouldHandleTargets(newState)) {
        PlayerUtils.handleSelectedTarget(newState, action.playerName)
      }
      break
    }

    return newState
  }

}