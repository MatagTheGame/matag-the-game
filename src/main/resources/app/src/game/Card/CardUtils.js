import {get} from 'lodash'

export default class CardUtils {
  static normalizeCardName(cardName) {
    return cardName.toLowerCase()
      .replace(/ /g, '_')
      .replace(/,/g, '_')
      .replace(/'/g, '')
  }

  static hasSummoningSickness(cardInstance) {
    return cardInstance.modifiers.summoningSickness
  }

  static hasAbility(cardInstance, ability) {
    return cardInstance.abilities.find((currentAbility) => currentAbility.abilityTypes.indexOf(ability) > -1)
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
    return cardInstance.modifiers.blocking === true
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
    if (!CardUtils.isOfType(attackingCard, 'CREATURE')) {
      return attackingCard.idAndName + ' is not of type Creature.'
    }

    if (CardUtils.hasSummoningSickness(attackingCard)) {
      return attackingCard.idAndName + ' has summoning sickness and cannot attack.'
    }

    if (CardUtils.isTapped(attackingCard)) {
      return attackingCard.idAndName + ' is already tapped and cannot attack.'
    }

    return true
  }

  static canBlock(blockingCard, blockedCard) {
    if (!CardUtils.isOfType(blockingCard, 'CREATURE')) {
      return blockingCard.idAndName + ' is not of type Creature.'
    }

    if (CardUtils.isTapped(blockingCard)) {
      return blockingCard.idAndName + ' is tapped and cannot block.'
    }

    if (CardUtils.hasAbility(blockedCard, 'FLYING')) {
      if (!(CardUtils.hasAbility(blockingCard, 'FLYING') || CardUtils.hasAbility(blockingCard, 'REACH'))) {
        return blockingCard.idAndName +' cannot block ' + blockedCard.idAndName + ' as it has flying.'
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

  static needsTargets(cardInstance, triggerType) {
    const ability = CardUtils.getAbilityForTriggerType(cardInstance, triggerType)
    if (ability) {
      return ability.targets.length > 0
    }
  }

  static getAbilityForTriggerType(cardInstance, triggerType) {
    return cardInstance.abilities.find(ability => get(ability, 'trigger.type') === triggerType)
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
}
