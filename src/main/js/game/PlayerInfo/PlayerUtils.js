import get from 'lodash/get'
import CardSearch from 'game/Card/CardSearch'
import CardUtils from 'game/Card/CardUtils'
import CostUtils from 'game/Card/CostUtils'
import TurnUtils from 'game/Turn/TurnUtils'
import UserInterfaceUtils from 'game/UserInterface/UserInterfaceUtils'
import stompClient from 'game/WebSocket'
import StackUtils from 'game/Stack/StackUtils'

export default class PlayerUtils {
  static isCurrentPlayerTurn(state) {
    return state.turn.currentTurnPlayer === state.player.name
  }

  static isCurrentPlayerActive(state) {
    return get(state, 'player.name') === get(state, 'turn.currentPhaseActivePlayer')
  }

  static getPlayerByName(state, playerName) {
    if (get(state, 'player.name') === playerName) {
      return state.player
    } else {
      return state.opponent
    }
  }

  static getCurrentPlayer(state) {
    return PlayerUtils.getPlayerByName(state, state.turn.currentTurnPlayer)
  }

  static getNonCurrentPlayer(state) {
    if (PlayerUtils.isCurrentPlayerTurn(state)) {
      return state.opponent
    } else {
      return state.player
    }
  }

  static getActivePlayer(state) {
    return PlayerUtils.getPlayerByName(state, state.turn.currentPhaseActivePlayer)
  }

  static isPlayerAbleToAttack(state) {
    const battlefield = PlayerUtils.getCurrentPlayer(state).battlefield
    return CardSearch.cards(battlefield)
      .ofType('CREATURE')
      .withoutSummoningSickness()
      .untapped()
      .isNotEmpty()
  }

  static isPlayerAbleToBlock(state) {
    const battlefield = PlayerUtils.getActivePlayer(state).battlefield
    return CardSearch.cards(battlefield)
      .ofType('CREATURE')
      .untapped()
      .isNotEmpty()
  }

  static castOrHandleTargets(newState, cardInstance, playedAbility) {
    const currentTappedMana = CostUtils.getMana(newState)
    if (CostUtils.isAbilityCostFulfilled(cardInstance, playedAbility, currentTappedMana)) {
      if (CardUtils.needsTargets(newState, [playedAbility])) {
        PlayerUtils.handleSelectTargets(newState, cardInstance, [playedAbility])
      } else {
        PlayerUtils.cast(newState, cardInstance.id, {}, playedAbility.abilityType)
      }
    }
  }

  static cast(state, cardId, targetsIdsForCardIds, playedAbility) {
    const mana = CostUtils.getMana(state)
    stompClient.sendEvent('turn', {action: 'CAST', cardIds: [cardId], mana: mana, targetsIdsForCardIds: targetsIdsForCardIds, playedAbility: playedAbility})
  }

  static castSelectedCard(state) {
    const playedAbilities = TurnUtils.getAbilitiesToBePlayed(state)
    PlayerUtils.cast(state, TurnUtils.getCardIdSelectedToBePlayed(state), {[TurnUtils.getCardIdSelectedToBePlayed(state)]: TurnUtils.getTargetsIds(state)}, playedAbilities[0])
    TurnUtils.resetTarget(state)
  }

  static handleSelectTargets(state, cardInstance, abilities) {
    if (TurnUtils.getCardIdSelectedToBePlayed(state) === cardInstance.id) {
      TurnUtils.resetTarget(state)
      UserInterfaceUtils.setStatusMessage(state, UserInterfaceUtils.PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE)
    } else {
      TurnUtils.selectCardToBePlayed(state, cardInstance, abilities)
      UserInterfaceUtils.setStatusMessage(state, `Select targets for ${cardInstance.card.name}.`)
    }
  }

  static shouldHandleTargets(state) {
    const abilities = PlayerUtils.getPlayedAbility(state)
    return CardUtils.needsTargets(state, abilities)
  }

  static getPlayedAbility(state) {
    if (TurnUtils.getCardIdSelectedToBePlayed(state)) {
      let cardInstance = CardSearch.cards(state.player.hand).withId(TurnUtils.getCardIdSelectedToBePlayed(state))
      if (cardInstance) {
        return CardUtils.getAbilitiesForTriggerType(cardInstance, 'CAST')

      } else {
        cardInstance = CardSearch.cards(state.player.battlefield).withId(TurnUtils.getCardIdSelectedToBePlayed(state))
        return CardUtils.getAbilitiesForTriggerType(cardInstance, 'ACTIVATED_ABILITY')
      }

    } else if (get(state, 'stack.length', 0) > 0) {
      return CardUtils.getAbilitiesForTriggerType(StackUtils.getTopSpell(state), 'TRIGGERED_ABILITY')
    }
  }

  static handleSelectedTarget(state, target) {
    TurnUtils.selectTarget(state, target)
    if (!PlayerUtils.shouldHandleTargets(state)) {
      if (TurnUtils.getCardIdSelectedToBePlayed(state)) {
        PlayerUtils.castSelectedCard(state)

      } else if (get(StackUtils.getTopSpell(state), 'triggeredAbilities[0].targets[0]')) {
        stompClient.sendEvent('turn', {action: 'RESOLVE', targetsIdsForCardIds: {[StackUtils.getTopSpell(state).id]: TurnUtils.getTargetsIds(state)}})
      }
    }
  }
}