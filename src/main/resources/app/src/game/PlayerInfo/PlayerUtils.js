import {get} from 'lodash'
import Phase from '../Turn/Phase'
import CardSearch from '../Card/CardSearch'
import StackUtils from '../Stack/StackUtils'
import stompClient from '../WebSocket'
import CostUtils from '../Card/CostUtils'

const CHOOSE_A_CARD_TO_DISCARD = 'Chose a card to discard.'
const CHOOSE_CREATURES_YOU_WANT_TO_BLOCK_WITH = 'Choose creatures you want to block with.'
const CHOOSE_CREATURES_YOU_WANT_TO_ATTACK_WITH = 'Choose creatures you want to attack with.'
const PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE = 'Play any spell or ability or continue (SPACE).'
const PLAY_ANY_INSTANT_OR_ABILITIES_OR_CONTINUE = 'Play any instant or ability or continue (SPACE).'
const PLAY_ANY_INSTANT_OR_ABILITIES_OR_CONTINUE_STACK_NOT_EMPTY = 'Play any instant or ability or resolve the top spell in the stack (SPACE).'
const WAIT_FOR_AN_OPPONENT_TO_PERFORM_ITS_ACTION = 'Wait for opponent to perform its action...'

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

  static setStatusMessage(state) {
    if (state.turn.winner) {
      state.message = {text: state.turn.winner + ' Win!', closable: true}

    } else if (!PlayerUtils.isCurrentPlayerActive(state)) {
      state.statusMessage = WAIT_FOR_AN_OPPONENT_TO_PERFORM_ITS_ACTION

    } else {
      if (!StackUtils.isStackEmpty(state)) {
        state.statusMessage = PLAY_ANY_INSTANT_OR_ABILITIES_OR_CONTINUE_STACK_NOT_EMPTY

      } else {
        if (Phase.isMainPhase(state.turn.currentPhase)) {
          state.statusMessage = PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE

        } else if (state.turn.currentPhase === 'DB' && PlayerUtils.isPlayerAbleToBlock(state)) {
          state.statusMessage = CHOOSE_CREATURES_YOU_WANT_TO_BLOCK_WITH

        } else if (state.turn.currentPhase === 'DA' && PlayerUtils.isPlayerAbleToAttack(state)) {
          state.statusMessage = CHOOSE_CREATURES_YOU_WANT_TO_ATTACK_WITH

        } else if (state.turn.triggeredNonStackAction) {
          state.statusMessage = CHOOSE_A_CARD_TO_DISCARD

        } else {
          state.statusMessage = PLAY_ANY_INSTANT_OR_ABILITIES_OR_CONTINUE
        }
      }
    }
  }

  static cast(state, cardId, targetsIdsForCardIds, playedAbility) {
    const currentManaIds = CostUtils.currentManaCardIds(state.player.battlefield)
    stompClient.sendEvent('turn', {action: 'CAST', cardIds: [cardId], tappingLandIds: currentManaIds, targetsIdsForCardIds: targetsIdsForCardIds, playedAbility: playedAbility})
  }

  static handleSelectTargets(state, cardInstance, ability) {
    if (state.turn.cardIdSelectedToBePlayed === cardInstance.id) {
      state.turn.cardIdSelectedToBePlayed = null
      state.turn.abilityToBePlayed = null
      state.statusMessage = PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE
    } else {
      state.turn.cardIdSelectedToBePlayed = cardInstance.id
      state.turn.abilityToBePlayed = ability
      state.statusMessage = 'Select targets for ' + cardInstance.card.name + '.'
    }
  }

  static handleSelectedTarget(state, target) {
    const targetsIds = PlayerUtils.getTargetsIds(target)
    const playedAbility = PlayerUtils.getAbilityToBePlayed(state.turn.abilityToBePlayed)
    PlayerUtils.cast(state, state.turn.cardIdSelectedToBePlayed, {[state.turn.cardIdSelectedToBePlayed]: targetsIds}, playedAbility)
    state.turn.cardIdSelectedToBePlayed = null
    state.turn.abilityToBePlayed = null
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