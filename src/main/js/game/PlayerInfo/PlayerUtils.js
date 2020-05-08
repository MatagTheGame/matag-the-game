import get from 'lodash/get'
import CardSearch from 'game/Card/CardSearch'
import CardUtils from 'game/Card/CardUtils'
import CostUtils from 'game/Card/CostUtils'
import TurnUtils from 'game/Turn/TurnUtils'
import UserInterfaceUtils from 'game/UserInterface/UserInterfaceUtils'
import stompClient from 'game/WebSocket'

export default class PlayerUtils {
  static isCurrentPlayerTurn(state) {
    return state.turn.currentTurnPlayer === state.player.name
  }

  static isCurrentPlayerActive(state) {
    return state.turn.currentPhaseActivePlayer === state.player.name
  }

  static getPlayerByName(state, playerName) {
    if (state.player.name === playerName) {
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

  static cast(state, cardId, targetsIdsForCardIds, playedAbility) {
    const mana = CostUtils.getMana(state)
    stompClient.sendEvent('turn', {action: 'CAST', cardIds: [cardId], mana: mana, targetsIdsForCardIds: targetsIdsForCardIds, playedAbility: playedAbility})
  }

  static castSelectedCard(state) {
    const playedAbility = TurnUtils.getAbilityToBePlayed(state)
    PlayerUtils.cast(state, TurnUtils.getCardIdSelectedToBePlayed(state), {[TurnUtils.getCardIdSelectedToBePlayed(state)]: TurnUtils.getTargetsIds(state)}, playedAbility)
    TurnUtils.resetTarget(state)
  }

  static handleSelectTargets(state, cardInstance, ability) {
    if (TurnUtils.getCardIdSelectedToBePlayed(state) === cardInstance.id) {
      TurnUtils.resetTarget(state)
      UserInterfaceUtils.setStatusMessage(state, UserInterfaceUtils.PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE)
    } else {
      TurnUtils.selectCardToBePlayed(state, cardInstance, ability)
      UserInterfaceUtils.setStatusMessage(state, `Select targets for ${cardInstance.card.name}.`)
    }
  }

  static shouldHandleTargets(state) {
    const ability = PlayerUtils.getPlayedAbility(state)
    return CardUtils.needsTargets(state, ability)
  }

  static getPlayedAbility(state) {
    if (TurnUtils.getCardIdSelectedToBePlayed(state)) {
      let cardInstance = CardSearch.cards(state.player.hand).withId(TurnUtils.getCardIdSelectedToBePlayed(state))
      if (cardInstance) {
        return CardUtils.getAbilitiesForTriggerType(cardInstance, 'CAST')[0]

      } else {
        cardInstance = CardSearch.cards(state.player.battlefield).withId(TurnUtils.getCardIdSelectedToBePlayed(state))
        return CardUtils.getAbilitiesForTriggerType(cardInstance, 'ACTIVATED_ABILITY')[0]
      }

    } else if (get(state, 'stack.length', 0) > 0) {
      return CardUtils.getAbilitiesForTriggerType(state.stack[0], 'TRIGGERED_ABILITY')[0]
    }
  }

  static handleSelectedTarget(state, target) {
    TurnUtils.selectTarget(state, target)
    if (!PlayerUtils.shouldHandleTargets(state)) {
      if (TurnUtils.getCardIdSelectedToBePlayed(state)) {
        PlayerUtils.castSelectedCard(state)

      } else if (get(state, 'stack[0].triggeredAbilities[0].targets[0]')) {
        stompClient.sendEvent('turn', {action: 'RESOLVE', targetsIdsForCardIds: {[get(state, 'stack[0].id')]: TurnUtils.getTargetsIds(state)}})
      }
    }
  }
}