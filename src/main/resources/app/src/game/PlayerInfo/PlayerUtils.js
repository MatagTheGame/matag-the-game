import Phase from '../Turn/Phase'
import CardSearch from '../Card/CardSearch'
import StackUtils from '../Stack/StackUtils'
import stompClient from '../WebSocket'
import CostUtils from '../Card/CostUtils'

const PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE = 'Play any spell or abilities or continue (SPACE).'
const PLAY_ANY_INSTANT_OR_ABILITIES_OR_CONTINUE = 'Play any instant or abilities or resolve the top spell in the stack (SPACE).'

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

  static canPlayerPerformAnyAction(state) {
    if (!PlayerUtils.isCurrentPlayerActive(state)) {
      state.statusMessage = 'Wait for opponent to perform its action...'
      return false
    }

    if (!PlayerUtils.isCurrentPlayerTurn(state)) {
      if (state.turn.currentPhase === 'DB' && PlayerUtils.isPlayerAbleToBlock(state)) {
        state.statusMessage = 'Choose creatures you want to block with.'
        return true
      } else {
        if (!StackUtils.isStackEmpty(state)) {
          state.statusMessage = PLAY_ANY_INSTANT_OR_ABILITIES_OR_CONTINUE
          return true
        }
      }

    } else {
      if (Phase.isMainPhase(state.turn.currentPhase)) {
        if (StackUtils.isStackEmpty(state)) {
          state.statusMessage = PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE
        } else {
          state.statusMessage = PLAY_ANY_INSTANT_OR_ABILITIES_OR_CONTINUE
        }
        return true
      } else if (state.turn.triggeredNonStackAction) {
        state.statusMessage = 'Chose a card to discard.'
        return true
      } else if (state.turn.currentPhase === 'DA' && PlayerUtils.isPlayerAbleToAttack(state)) {
        state.statusMessage = 'Choose creatures you want to attack with.'
        return true
      } else {
        state.statusMessage = '...'
        return false
      }
    }
  }

  static cast(state, cardId, targetsIdsForCardIds) {
    const currentManaIds = CostUtils.currentManaCardIds(state.player.battlefield)
    stompClient.sendEvent('turn', {action: 'CAST', cardIds: [cardId], tappingLandIds: currentManaIds, targetsIdsForCardIds: targetsIdsForCardIds})
  }

  static handleSelectTargets(state, cardInstance) {
    if (state.turn.cardIdSelectedToBePlayed === cardInstance.id) {
      state.turn.cardIdSelectedToBePlayed = null
      state.statusMessage = PLAY_ANY_SPELL_OR_ABILITIES_OR_CONTINUE
    } else {
      state.turn.cardIdSelectedToBePlayed = cardInstance.id
      state.statusMessage = 'Select targets for ' + cardInstance.card.name + '.'
    }
  }

  static handleSelectedTarget(state, target) {
    const targetsIds = []
    if (typeof target === 'string') {
      targetsIds.push(target)
    } else {
      targetsIds.push(target.id)
    }
    PlayerUtils.cast(state, state.turn.cardIdSelectedToBePlayed, {[state.turn.cardIdSelectedToBePlayed]: targetsIds})
    state.turn.cardIdSelectedToBePlayed = null
  }
}