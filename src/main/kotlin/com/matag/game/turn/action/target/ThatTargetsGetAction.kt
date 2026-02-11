package com.matag.game.turn.action.target

import com.matag.cards.ability.Ability
import com.matag.cards.ability.AbilityAction
import com.matag.cards.ability.selector.SelectorType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.permanent.PermanentGetService
import com.matag.game.turn.action.player.PlayerGetService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ThatTargetsGetAction(
    private val targetCheckerService: TargetCheckerService,
    private val permanentGetService: PermanentGetService,
    private val playerGetService: PlayerGetService
) : AbilityAction {

    override fun perform(cardInstance: CardInstance, gameStatus: GameStatus, ability: Ability?) {
        for (i in ability!!.targets.indices) {
            val target = ability.targets.get(i)
            val targetId = targetCheckerService!!.getTargetIdAtIndex(cardInstance, ability, i)

            if (targetId != null) {
                when (target.magicInstanceSelector!!.selectorType) {
                    SelectorType.PERMANENT -> thatPermanentGets(gameStatus, cardInstance, ability, targetId as Int)
                    SelectorType.PLAYER -> thatPlayerGets(gameStatus, cardInstance, ability, targetId as String)
                    SelectorType.SPELL -> thatSpellGets(gameStatus, cardInstance, ability, targetId as Int)
                    SelectorType.ANY -> thatAnyTargetGets(cardInstance, gameStatus, ability, targetId)
                    else -> throw IllegalArgumentException("${target.javaClass.name} is not a valid target")
                }
            }
        }
    }

    private fun thatPlayerGets(
        gameStatus: GameStatus,
        cardInstance: CardInstance,
        ability: Ability,
        targetPlayer: String?
    ) {
        val player = gameStatus.getPlayerByName(targetPlayer)
        playerGetService!!.thatPlayerGets(cardInstance, gameStatus, ability.parameters, player)
    }

    private fun thatPermanentGets(gameStatus: GameStatus, cardInstance: CardInstance, ability: Ability, targetId: Int) {
        val targetOptional = gameStatus.allBattlefieldCardsSearch.withId(targetId)
        thatTargetGets(gameStatus, cardInstance, ability, targetId, targetOptional)
    }

    private fun thatSpellGets(gameStatus: GameStatus, cardInstance: CardInstance, ability: Ability, targetId: Int) {
        val targetOptional = gameStatus.stack.search().withId(targetId)
        thatTargetGets(gameStatus, cardInstance, ability, targetId, targetOptional)
    }

    private fun thatAnyTargetGets(
        cardInstance: CardInstance,
        gameStatus: GameStatus,
        ability: Ability,
        targetId: Any?
    ) {
        if (targetId is String) {
            thatPlayerGets(gameStatus, cardInstance, ability, targetId)
        } else if (targetId is Int) {
            thatPermanentGets(gameStatus, cardInstance, ability, targetId)
        }
    }

    private fun thatTargetGets(
        gameStatus: GameStatus,
        cardInstance: CardInstance,
        ability: Ability,
        targetCardId: Int,
        targetOptional: CardInstance?
    ) {
        if (targetOptional != null) {
            val target = targetOptional
            permanentGetService.thatPermanentGets(cardInstance, gameStatus, ability.parameters, target)
        } else {
            LOGGER.info("target {} is not anymore valid.", targetCardId)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ThatTargetsGetAction::class.java)
    }
}
