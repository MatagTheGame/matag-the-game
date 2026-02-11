package com.matag.game.turn.action.target

import com.matag.cards.ability.Ability
import com.matag.cards.ability.selector.SelectorType
import com.matag.cards.ability.target.Target
import com.matag.cards.ability.trigger.TriggerType
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.message.MessageException
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService
import com.matag.player.PlayerType
import org.springframework.stereotype.Component
import kotlin.math.min

@Component
class TargetCheckerService(
    private val magicInstancePermanentSelectorService: MagicInstancePermanentSelectorService
) {

    fun checkSpellOrAbilityTargetRequisites(
        cardToCast: CardInstance,
        gameStatus: GameStatus,
        targetsIdsForCardIds: Map<Int, List<Any>>,
        playedAbility: String?
    ) {
        val playedAbilities = playedAbilities(cardToCast, playedAbility)
        val targetsIds = getTargetsIds(targetsIdsForCardIds, cardToCast.id)

        var targetIndex = 0
        for (ability in playedAbilities) {
            if (!ability!!.targets.isEmpty()) {
                checkThatTargetsAreDifferent(ability.targets, targetsIds)
                var i = 0
                while (i < ability.targets.size) {
                    val targetId = if (targetIndex < targetsIds.size) targetsIds.get(targetIndex) else null
                    check(gameStatus, cardToCast, ability.targets.get(i), targetId)
                    i++
                    targetIndex++
                }

                cardToCast.modifiers.addTargets(targetsIds)
            }
        }
    }

    fun checkIfRequiresTarget(cardToCast: CardInstance): Boolean {
        for (ability in cardToCast.getAbilitiesByType(AbilityType.THAT_TARGETS_GET)) {
            return !ability!!.targets.isEmpty()
        }

        return false
    }

    fun checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(
        cardToCast: CardInstance,
        gameStatus: GameStatus
    ): Boolean {
        for (ability in cardToCast.getAbilitiesByType(AbilityType.THAT_TARGETS_GET)) {
            for (target in ability.targets) {
                if (target.magicInstanceSelector!!.selectorType == SelectorType.PLAYER) {
                    return true
                } else {
                    val possibleTargets = magicInstancePermanentSelectorService.selectAsTarget(
                        gameStatus,
                        cardToCast,
                        target.magicInstanceSelector!!
                    )
                    if (possibleTargets.isNotEmpty()) {
                        return true
                    }
                }
            }
        }

        return false
    }

    fun check(gameStatus: GameStatus, cardInstance: CardInstance, target: Target, targetId: Any?) {
        val magicInstanceSelector = target.magicInstanceSelector
        if (targetId == null) {
            if (!target.optional) {
                throw MessageException(cardInstance.idAndName + " requires a valid target.")
            }
        } else if (targetId is String) {
            if (!(magicInstanceSelector!!.selectorType == SelectorType.PLAYER || magicInstanceSelector.selectorType == SelectorType.ANY)) {
                throw MessageException(targetId.toString() + " is not valid for type " + magicInstanceSelector.selectorType)
            }

            if (PlayerType.OPPONENT == magicInstanceSelector.controllerType && cardInstance.controller == targetId) {
                throw MessageException(targetId.toString() + " is not valid for type " + magicInstanceSelector.selectorType + " (needs to be an opponent)")
            }
        } else {
            if (magicInstanceSelector!!.selectorType == SelectorType.PLAYER) {
                throw MessageException(targetId.toString() + " is not valid for type " + magicInstanceSelector.selectorType)
            }

            val targetCardId = targetId as Int
            val possibleTargets = magicInstancePermanentSelectorService!!.selectAsTarget(
                gameStatus,
                cardInstance,
                magicInstanceSelector
            )
            if (possibleTargets.withId(targetCardId) == null) {
                throw MessageException("Selected targets were not valid.")
            }
        }
    }

    fun getTargetIdAtIndex(cardInstance: CardInstance, ability: Ability?, index: Int): Any? {
        val abilityIndex = cardInstance.abilities.indexOf(ability)
        var firstTargetIndex = 0
        for (i in 0..<abilityIndex) {
            firstTargetIndex += cardInstance.abilities[i].targets.size
        }

        if (cardInstance.modifiers.targets.size > firstTargetIndex + index) {
            return cardInstance.modifiers.targets[firstTargetIndex + index]
        }

        return null
    }

    private fun playedAbilities(cardToCast: CardInstance, playedAbility: String?): List<Ability> {
        if (playedAbility != null) {
            return cardToCast.getAbilitiesByType(AbilityType.valueOf(playedAbility))
        } else {
            return cardToCast.getAbilitiesByTriggerType(TriggerType.CAST)
        }
    }

    private fun getTargetsIds(
        targetsIdsForCardIds: Map<Int, List<Any>>,
        cardId: Int
    ): List<Any> {
        return targetsIdsForCardIds.getOrDefault(cardId, listOf())
    }

    private fun checkThatTargetsAreDifferent(targets: List<Target>, targetsIds: List<Any>) {
        for (i in 0..<min(targets.size, targetsIds.size)) {
            val target = targets.get(i)
            if (target.other) {
                if (countTargetsWithId(targetsIds, targetsIds.get(i)) > 1) {
                    throw MessageException("Targets must be different.")
                }
            }
        }
    }

    private fun countTargetsWithId(targetsIds: List<Any>, targetId: Any?): Long {
        return targetsIds.stream().filter { it == targetId }.count()
    }
}
