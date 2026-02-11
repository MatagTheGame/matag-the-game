package com.matag.game.turn.action.selection

import com.matag.cards.ability.selector.MagicInstanceSelector
import com.matag.cards.ability.selector.SelectorType
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
        cardInstance: CardInstance,
        magicInstanceSelector: MagicInstanceSelector
    ): CardInstanceSearch {
        var cards: CardInstanceSearch
        if (magicInstanceSelector.selectorType == SelectorType.PERMANENT) {
            cards = gameStatus.allBattlefieldCardsSearch
        } else if (magicInstanceSelector.selectorType == SelectorType.SPELL) {
            cards = gameStatus.stack.search()
        } else if (magicInstanceSelector.selectorType == SelectorType.ANY) {
            cards = gameStatus.allBattlefieldCardsSearch
        } else {
            throw RuntimeException("Missing selectorType.")
        }

        if (magicInstanceSelector.ofType != null) {
            cards = cards.ofAnyOfTheTypes(magicInstanceSelector.ofType!!)
        } else if (magicInstanceSelector.notOfType != null) {
            cards = cards.notOfTypes(magicInstanceSelector.notOfType!!)
        }

        if (magicInstanceSelector.ofAllTypes != null) {
            cards = cards.ofAllOfTheTypes(magicInstanceSelector.ofAllTypes!!)
        }

        if (magicInstanceSelector.ofSubtype != null) {
            cards = cards.ofAnyOfTheSubtypes(magicInstanceSelector.ofSubtype!!)
        } else if (magicInstanceSelector.notOfSubtype != null) {
            cards = cards.notOfSubtypes(magicInstanceSelector.notOfSubtype!!)
        }

        if (magicInstanceSelector.controllerType == PlayerType.PLAYER) {
            cards = cards.controlledBy(cardInstance.controller)
        } else if (magicInstanceSelector.controllerType == PlayerType.OPPONENT) {
            cards = cards.controlledBy(gameStatus.getOtherPlayer(cardInstance.controller).name)
        }

        if (magicInstanceSelector.withAbilityType != null) {
            cards = cards.withFixedAbility(magicInstanceSelector.withAbilityType)
        }

        if (magicInstanceSelector.withoutAbilityType != null) {
            cards = cards.withoutFixedAbility(magicInstanceSelector.withoutAbilityType)
        }

        if (magicInstanceSelector.powerToughnessConstraint != null) {
            cards = cards.ofPowerToughnessConstraint(magicInstanceSelector.powerToughnessConstraint!!)
        }

        if (magicInstanceSelector.statusTypes != null) {
            if (magicInstanceSelector.statusTypes!!.contains(StatusType.ATTACKING) && magicInstanceSelector.statusTypes!!.contains(
                    StatusType.BLOCKING
                )
            ) {
                cards = cards.attackingOrBlocking()
            } else if (magicInstanceSelector.statusTypes!!.contains(StatusType.ATTACKING)) {
                cards = cards.attacking()
            } else if (magicInstanceSelector.statusTypes!!.contains(StatusType.BLOCKING)) {
                cards = cards.blocking()
            }

            if (magicInstanceSelector.statusTypes!!.contains(StatusType.ATTACKED) && magicInstanceSelector.statusTypes!!.contains(
                    StatusType.BLOCKED
                )
            ) {
                cards = cards.attackedOrBlocked()
            } else if (magicInstanceSelector.statusTypes!!.contains(StatusType.ATTACKED)) {
                cards = cards.attacked()
            } else if (magicInstanceSelector.statusTypes!!.contains(StatusType.BLOCKED)) {
                cards = cards.blocked()
            }

            if (magicInstanceSelector.statusTypes!!.contains(StatusType.TAPPED)) {
                cards = cards.tapped()
            }
        }

        if (magicInstanceSelector.ofColors != null) {
            cards = cards.ofAnyOfTheColors(magicInstanceSelector.ofColors!!)
        }

        if (magicInstanceSelector.colorless) {
            cards = cards.colorless()
        }

        if (magicInstanceSelector.multicolor) {
            cards = cards.multicolor()
        }

        if (magicInstanceSelector.others) {
            cards = cards.notWithId(cardInstance.id)
        }

        if (magicInstanceSelector.itself) {
            cards = cards.withIdAsList(cardInstance.id)
        }

        if (magicInstanceSelector.turnStatusType != null) {
            cards = cards.onTurnStatusType(magicInstanceSelector.turnStatusType!!, gameStatus)
        }

        if (magicInstanceSelector.currentEnchanted) {
            cards = cards.withIdAsList((cardInstance.modifiers.attachedToId))
        }

        if (magicInstanceSelector.historic) {
            cards = cards.historic()
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
