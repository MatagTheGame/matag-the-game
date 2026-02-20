package com.matag.game.turn.action.selection

import com.matag.cards.ability.selector.MagicInstanceSelector
import com.matag.cards.ability.selector.SelectorType.*
import com.matag.cards.ability.selector.StatusType
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceSearch
import com.matag.game.status.GameStatus
import com.matag.player.PlayerType
import org.springframework.stereotype.Component

@Component
class MagicInstancePermanentSelectorService {
    fun select(
        gameStatus: GameStatus,
        cardInstance: CardInstance?,
        magicInstanceSelector: MagicInstanceSelector
    ): CardInstanceSearch {
        var cards: CardInstanceSearch

        cards = when (magicInstanceSelector.selectorType) {
            PERMANENT -> gameStatus.allBattlefieldCardsSearch
            SPELL -> gameStatus.stack.search()
            ANY -> gameStatus.allBattlefieldCardsSearch
            else -> throw RuntimeException("Missing selectorType.")
        }

        magicInstanceSelector.ofType?.let { cards = cards.ofAnyOfTheTypes(it) }
        magicInstanceSelector.notOfType?.let { cards = cards.notOfTypes(it) }
        magicInstanceSelector.ofAllTypes?.let { cards = cards.ofAllOfTheTypes(it) }

        magicInstanceSelector.ofSubtype?.let{ cards = cards.ofAnyOfTheSubtypes(it) }
        magicInstanceSelector.notOfSubtype?.let { cards = cards.notOfSubtypes(it) }

        magicInstanceSelector.withAbilityType?.let { cards = cards.withFixedAbility(it) }
        magicInstanceSelector.withoutAbilityType?.let { cards = cards.withoutFixedAbility(it) }

        magicInstanceSelector.powerToughnessConstraint?.let { cards = cards.ofPowerToughnessConstraint(it) }

        magicInstanceSelector.ofColors?.let { cards = cards.ofAnyOfTheColors(it) }
        if (magicInstanceSelector.colorless) { cards = cards.colorless() }
        if (magicInstanceSelector.multicolor) { cards = cards.multicolor() }

        if (magicInstanceSelector.historic) { cards = cards.historic() }

        magicInstanceSelector.statusTypes?.let {
            if (it.contains(StatusType.ATTACKING) && it.contains(StatusType.BLOCKING)) {
                cards = cards.attackingOrBlocking()
            } else if (it.contains(StatusType.ATTACKING)) {
                cards = cards.attacking()
            } else if (it.contains(StatusType.BLOCKING)) {
                cards = cards.blocking()
            }

            if (it.contains(StatusType.ATTACKED) && it.contains(StatusType.BLOCKED)) {
                cards = cards.attackedOrBlocked()
            } else if (it.contains(StatusType.ATTACKED)) {
                cards = cards.attacked()
            } else if (it.contains(StatusType.BLOCKED)) {
                cards = cards.blocked()
            }

            if (it.contains(StatusType.TAPPED)) {
                cards = cards.tapped()
            }
        }

        magicInstanceSelector.turnStatusType?.let { cards = cards.onTurnStatusType(it, gameStatus) }

        cardInstance?.let {
            if (magicInstanceSelector.controllerType == PlayerType.PLAYER) {
                cards = cards.controlledBy(cardInstance.controller)
            } else if (magicInstanceSelector.controllerType == PlayerType.OPPONENT) {
                cards = cards.controlledBy(gameStatus.getOtherPlayer(cardInstance.controller).name)
            }

            if (magicInstanceSelector.others) {
                cards = cards.notWithId(cardInstance.id)
            }

            if (magicInstanceSelector.itself) {
                cards = cards.withIdAsList(cardInstance.id)
            }

            if (magicInstanceSelector.currentEnchanted) {
                cards = cards.withIdAsList((cardInstance.modifiers.attachedToId))
            }
        }

        return cards
    }

    fun selectAsTarget(
        gameStatus: GameStatus,
        cardInstance: CardInstance,
        magicInstanceSelector: MagicInstanceSelector
    ): CardInstanceSearch {
        val cards = select(gameStatus, cardInstance, magicInstanceSelector).cards
        val playerCards = CardInstanceSearch(cards).controlledBy(cardInstance.controller)
        val opponentCardsWithoutHexproof =
            CardInstanceSearch(cards).notControlledBy(cardInstance.controller).withoutFixedAbility(AbilityType.HEXPROOF)
        return playerCards.concat(opponentCardsWithoutHexproof)
    }
}
