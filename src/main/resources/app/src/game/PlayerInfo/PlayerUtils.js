import get from 'lodash/get'
import stompClient from 'Main/game/WebSocket'
import CardSearch from 'Main/game/Card/CardSearch'
import CostUtils from 'Main/game/Card/CostUtils'
import UserInterfaceUtils from 'Main/game/UserInterface/UserInterfaceUtils'


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

  static handleSelectTargets(state, cardInstance, ability) {
    if (state.turn.cardIdSelectedToBePlayed === cardInstance.id) {
      state.turn.cardIdSelectedToBePlayed = null
      state.turn.abilityToBePlayed = null
      UserInterfaceUtils.setStatusMessage(state, UserInterfaceUtils.PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE)
    } else {
      state.turn.cardIdSelectedToBePlayed = cardInstance.id
      state.turn.abilityToBePlayed = ability
      UserInterfaceUtils.setStatusMessage(state, `Select targets for ${cardInstance.card.name}.`)
    }
  }

  static shouldHandleTargets(state) {
    return state.turn.cardIdSelectedToBePlayed || get(state, 'stack[0].triggeredAbilities[0].targets[0]')
  }

  static handleSelectedTarget(state, target) {
    const targetsIds = PlayerUtils.getTargetsIds(target)
    if (state.turn.cardIdSelectedToBePlayed) {
      const playedAbility = PlayerUtils.getAbilityToBePlayed(state.turn.abilityToBePlayed)
      PlayerUtils.cast(state, state.turn.cardIdSelectedToBePlayed, {[state.turn.cardIdSelectedToBePlayed]: targetsIds}, playedAbility)
      state.turn.cardIdSelectedToBePlayed = null
      state.turn.abilityToBePlayed = null

    } else if (get(state, 'stack[0].triggeredAbilities[0].targets[0]')) {
      stompClient.sendEvent('turn', {action: 'RESOLVE', targetsIdsForCardIds: {[get(state, 'stack[0].id')]: targetsIds}})
    }
  }

  static getTargetsIds(target) {
    const targetsIds = []
    if (typeof target === 'string') {
      targetsIds.push(target)
    } else {
      targetsIds.push(target.id)
    }
    return targetsIds
  }

  static getAbilityToBePlayed(abilityToBePlayed) {
    return get(abilityToBePlayed, 'abilityTypes[0]')
  }
}