import get from 'lodash/get'
import CostUtils from 'Main/game/Card/CostUtils'
import {TurnUtils} from 'Main/game/Turn/TurnUtils'

export default class CardUtils {
  static normalizeCardName(cardName) {
    return cardName.toLowerCase()
      .replace(/ /g, '_')
      .replace(/-/g, '_')
      .replace(/,/g, '')
      .replace(/'/g, '')
  }

  static getIdAndName(cardInstance) {
    return `"${cardInstance.id} - ${cardInstance.card.name}"`
  }

  static hasSummoningSickness(cardInstance) {
    return cardInstance.summoningSickness
  }

  static hasAbility(cardInstance, abilityType) {
    return this.getAbilities(cardInstance, abilityType).length > 0
  }

  static getAbilities(cardInstance, abilityType) {
    return cardInstance.abilities.filter((currentAbility) => currentAbility.abilityType === abilityType)
  }

  static frontendTap(cardInstance) {
    cardInstance.modifiers.tapped = 'FRONTEND_TAPPED'
  }

  static untap(cardInstance) {
    cardInstance.modifiers.tapped = undefined
  }

  static isFrontendTapped(cardInstance) {
    return cardInstance.modifiers.tapped === 'FRONTEND_TAPPED'
  }

  static toggleFrontendTapped(cardInstance) {
    if (CardUtils.isUntapped(cardInstance)) {
      CardUtils.frontendTap(cardInstance)
    } else if (CardUtils.isFrontendTapped(cardInstance)) {
      CardUtils.untap(cardInstance)
    }
  }

  static isNotFrontendBlocking(cardInstance) {
    return !CardUtils.isFrontendBlocking(cardInstance)
  }

  static isFrontendBlocking(cardInstance) {
    return cardInstance.modifiers.blocking === 'FRONTEND'
  }

  static setFrontendBlocking(cardInstance, blockingCardId) {
    cardInstance.modifiers.blocking = 'FRONTEND'
    cardInstance.modifiers.blockingCardId = blockingCardId
  }

  static setFrontendUnblocking(cardInstance) {
    cardInstance.modifiers.blocking = undefined
    cardInstance.modifiers.blockingCardId = undefined
  }

  static isNotFrontendAttacking(cardInstance) {
    return !CardUtils.isFrontendAttacking(cardInstance)
  }

  static isFrontendAttacking(cardInstance) {
    return cardInstance.modifiers.attacking === 'FRONTEND'
  }

  static setFrontendAttacking(cardInstance) {
    cardInstance.modifiers.attacking = 'FRONTEND'
  }

  static setFrontendUnattacking(cardInstance) {
    cardInstance.modifiers.attacking = undefined
  }

  static isBlocking(cardInstance) {
    return cardInstance.modifiers.blockingCardId
  }

  static isBlockingOrFrontendBlocking(cardInstance) {
    return CardUtils.isBlocking(cardInstance) || CardUtils.isFrontendBlocking(cardInstance)
  }

  static isNotBlockingOrFrontendBlocking(cardInstance) {
    return CardUtils.isNotBlocking(cardInstance) && CardUtils.isNotFrontendBlocking(cardInstance)
  }

  static isNotBlocking(cardInstance) {
    return !CardUtils.isBlocking(cardInstance)
  }

  static isAttacking(cardInstance) {
    return cardInstance.modifiers.attacking === true
  }

  static isAttackingOrFrontendAttacking(cardInstance) {
    return CardUtils.isAttacking(cardInstance) || CardUtils.isFrontendAttacking(cardInstance)
  }

  static isNotAttacking(cardInstance) {
    return !CardUtils.isAttacking(cardInstance)
  }

  static isNotAttackingOrFrontendAttacking(cardInstance) {
    return CardUtils.isNotAttacking(cardInstance) && CardUtils.isNotFrontendAttacking(cardInstance)
  }

  static toggleFrontendBlocking(cardInstance, blockingCardId) {
    if (CardUtils.isNotFrontendBlocking(cardInstance)) {
      CardUtils.setFrontendBlocking(cardInstance, blockingCardId)
    } else if (CardUtils.isFrontendBlocking(cardInstance)) {
      CardUtils.setFrontendUnblocking(cardInstance)
    }
  }

  static toggleFrontendAttacking(cardInstance) {
    if (CardUtils.isNotFrontendAttacking(cardInstance)) {
      CardUtils.setFrontendAttacking(cardInstance)
    } else if (CardUtils.isFrontendAttacking(cardInstance)) {
      CardUtils.setFrontendUnattacking(cardInstance)
    }

    if (!CardUtils.hasAbility(cardInstance, 'VIGILANCE')) {
      CardUtils.toggleFrontendTapped(cardInstance)
    }
  }

  static isUntapped(cardInstance) {
    return !CardUtils.isTappedOrFrontendTapped(cardInstance)
  }

  static isTapped(cardInstance) {
    return cardInstance.modifiers.tapped === 'TAPPED'
  }

  static isTappedOrFrontendTapped(cardInstance) {
    return CardUtils.isTapped(cardInstance) || CardUtils.isFrontendTapped(cardInstance)
  }

  static doesNotUntapNextTurn(cardInstance) {
    return cardInstance.modifiers.doesNotUntapNextTurn
  }

  static isOfType(cardInstance, type) {
    return cardInstance.card.types.indexOf(type) >= 0
  }

  static isOfAnyType(cardInstance, types) {
    return cardInstance.card.types.some(type => types.indexOf(type) >= 0)
  }

  static isOfSubtype(cardInstance, subtype) {
    return cardInstance.card.subtypes.indexOf(subtype) >= 0
  }

  static canAttack(attackingCard) {
    let attackingCardName = CardUtils.getIdAndName(attackingCard)
    if (!CardUtils.isOfType(attackingCard, 'CREATURE')) {
      return `${attackingCardName} is not of type Creature.`
    }

    if (CardUtils.hasSummoningSickness(attackingCard)) {
      return `${attackingCardName} has summoning sickness and cannot attack.`
    }

    if (CardUtils.isTapped(attackingCard)) {
      return `${attackingCardName} is already tapped and cannot attack.`
    }

    return true
  }

  static canBlock(blockingCard, blockedCard) {
    let blockingCardName = CardUtils.getIdAndName(blockingCard)
    let blockedCardName = CardUtils.getIdAndName(blockedCard)
    if (!CardUtils.isOfType(blockingCard, 'CREATURE')) {
      return `${blockedCardName} is not of type Creature.`
    }

    if (CardUtils.isTapped(blockingCard)) {
      return `${blockedCardName} is tapped and cannot block.`
    }

    if (CardUtils.hasAbility(blockedCard, 'FLYING')) {
      if (!(CardUtils.hasAbility(blockingCard, 'FLYING') || CardUtils.hasAbility(blockingCard, 'REACH'))) {
        return `${blockingCardName} cannot block ${blockedCardName} as it has flying.`
      }
    }

    return true
  }

  static blockingCreaturesToTargetIdsEvent(blockingCreatures) {
    const map = {}

    blockingCreatures.forEach(blockingCreature => {
      map[blockingCreature.id] = [blockingCreature.modifiers.blockingCardId]
    })

    return map
  }

  static needsTargets(state, cardInstance, triggerType) {
    let ability
    if (triggerType === 'TRIGGERED_ABILITY') {
      ability = get(cardInstance, 'triggeredAbilities[0]')
    } else {
      ability = CardUtils.getAbilitiesForTriggerType(cardInstance, triggerType)[0]
    }
    if (ability) {
      return ability.targets.length > TurnUtils.getTargetsIds(state).length
    }
  }

  static getAbilitiesForTriggerType(cardInstance, triggerType) {
    return cardInstance.abilities.filter(ability => get(ability, 'trigger.type') === triggerType)
  }

  static getAbilitiesForTriggerTypes(cardInstance, triggerTypes) {
    return cardInstance.abilities.filter(ability => triggerTypes.indexOf(get(ability, 'trigger.type')) > -1)
  }

  static getPower(cardInstance) {
    // already contains the modifier extraPowerAndToughnessUntilEndOfTurn
    return cardInstance.power
  }

  static getToughness(cardInstance) {
    // already contains the modifier extraPowerAndToughnessUntilEndOfTurn
    return cardInstance.toughness
  }

  static getDamage(cardInstance) {
    return cardInstance.modifiers.damage
  }

  static isAttachedToId(cardInstance, id) {
    return cardInstance.modifiers.attachedToId === id
  }

  static isNotAttached(cardInstance) {
    return !cardInstance.modifiers.attachedToId
  }

  static activateManaAbility(state, cardInstance, index = 0) {
    if (CardUtils.isFrontendTapped(cardInstance)) {
      CostUtils.removeMana(state, cardInstance.id)
    } else {
      CostUtils.addMana(state, cardInstance.id, CardUtils.getAbilities(cardInstance, 'TAP_ADD_MANA')[index].parameters)
    }
    CardUtils.toggleFrontendTapped(cardInstance)
  }

  static getPlus1Counters(cardInstance) {
    return cardInstance.modifiers.counters.plus1Counters;
  }
}
