package com.matag.game.turn.action._continue

import com.matag.cards.ability.Ability
import com.matag.cards.ability.trigger.TriggerType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.message.MessageException
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.AbilityActionFactory
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService
import com.matag.game.turn.action.leave.PutIntoGraveyardService
import com.matag.game.turn.action.player.DiscardXCardsService
import com.matag.game.turn.action.player.ScryXCardsService
import com.matag.game.turn.action.target.TargetCheckerService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Consumer
import java.util.stream.Collectors

@Component
class ResolveService(
    private val continueService: ContinueService,
    private val abilityActionFactory: AbilityActionFactory,
    private val enterCardIntoBattlefieldService: EnterCardIntoBattlefieldService,
    private val putIntoGraveyardService: PutIntoGraveyardService,
    private val targetCheckerService: TargetCheckerService,
    private val discardXCardsService: DiscardXCardsService,
    private val scryXCardsService: ScryXCardsService
) {
    
    fun resolve(
        gameStatus: GameStatus,
        inputRequiredAction: String,
        inputRequiredChoices: String?,
        targetCardIds: List<Int>,
        targetsIdsForCardIds: Map<Int, List<Any>>
    ) {
        if (!gameStatus.stack.isEmpty()) {
            val stackItemToResolve = gameStatus.stack.peek()!!

            if (stackItemToResolve.triggeredAbilities.isEmpty()) {
                gameStatus.stack.pop()
                resolveCardInstanceFromStack(gameStatus, stackItemToResolve)
            } else {
                val controllerName = stackItemToResolve.controller
                val otherPlayerName = gameStatus.getOtherPlayer(controllerName).name

                stackItemToResolve.triggeredAbilities.stream()
                    .filter({ triggeredAbility -> triggeredAbility.trigger?.type != TriggerType.TRIGGERED_ABILITY })
                    .forEach({ t -> stackItemToResolve.acknowledgedBy(controllerName!!) })

                if (gameStatus.activePlayer.name == controllerName && !stackItemToResolve.acknowledgedBy.contains(controllerName)) {
                    if (targetCheckerService.checkIfRequiresTarget(stackItemToResolve)) {
                        if (targetCheckerService.checkIfValidTargetsArePresentForSpellOrAbilityTargetRequisites(
                                stackItemToResolve,
                                gameStatus
                            )
                        ) {
                            targetCheckerService.checkSpellOrAbilityTargetRequisites(
                                stackItemToResolve,
                                gameStatus,
                                targetsIdsForCardIds,
                                "THAT_TARGETS_GET"
                            )
                        }
                    }
                    stackItemToResolve.acknowledgedBy(controllerName!!)
                } else if (!stackItemToResolve.acknowledgedBy.contains(otherPlayerName)) {
                    val needsTargets =  stackItemToResolve.triggeredAbilities.flatMap{it.targets}.isNotEmpty()
                    val hasSelectedTargets: Boolean = !stackItemToResolve.modifiers.targets.isEmpty()
                    if (!needsTargets || hasSelectedTargets) {
                        stackItemToResolve.acknowledgedBy(otherPlayerName!!)
                    }
                }

                if (stackItemToResolve.acknowledged()) {
                    gameStatus.stack.pop()
                    resolveTriggeredAbility(gameStatus, stackItemToResolve)
                }
            }

            gameStatus.turn.currentPhaseActivePlayer = gameStatus.nonActivePlayer?.name
        } else if (gameStatus.turn.inputRequiredAction == inputRequiredAction) {
            resolveInputRequiredAction(gameStatus, inputRequiredAction, inputRequiredChoices, targetCardIds)
        } else {
            val message = "Cannot resolve inputRequiredAction " + inputRequiredAction + " as current inputRequiredAction is " + gameStatus.turn.inputRequiredAction
            throw MessageException(message)
        }
    }

    private fun resolveCardInstanceFromStack(gameStatus: GameStatus, cardToResolve: CardInstance) {
        performAbilitiesActions(gameStatus, cardToResolve, cardToResolve.getAbilitiesByTriggerType(TriggerType.CAST))

        if (cardToResolve.isPermanent) {
            enterCardIntoBattlefieldService!!.enter(gameStatus, cardToResolve)
        } else {
            cardToResolve.resetAllModifiers()
            putIntoGraveyardService!!.putIntoGraveyard(gameStatus, cardToResolve)
        }
    }

    private fun resolveTriggeredAbility(gameStatus: GameStatus?, stackItemToResolve: CardInstance) {
        performAbilitiesActions(gameStatus, stackItemToResolve, stackItemToResolve.triggeredAbilities)
        stackItemToResolve.triggeredAbilities = listOf()
    }

    private fun resolveInputRequiredAction(
        gameStatus: GameStatus,
        inputRequiredAction: String?,
        inputRequiredChoices: String?,
        cardIds: List<Int>?
    ) {
        if (InputRequiredActions.DISCARD_A_CARD == inputRequiredAction) {
            discardXCardsService.discardXCards(cardIds!!, gameStatus)
        } else if (InputRequiredActions.SCRY == inputRequiredAction) {
            val positions = extractIntegerChoices(inputRequiredChoices)
            if (positions.size == gameStatus.turn.inputRequiredActionParameter?.toInt()) {
                scryXCardsService.scryXCards(gameStatus, positions)
            } else {
                throw MessageException("Please click on the visible cards to scry them.")
            }
        }

        gameStatus.turn.inputRequiredAction = null
        gameStatus.turn.inputRequiredActionParameter = null
        continueService.next(gameStatus)
    }

    private fun extractIntegerChoices(inputRequiredChoices: String?): List<Int> {
        if (inputRequiredChoices == null) {
            return ArrayList<Int>()
        }
        return Arrays.stream(inputRequiredChoices.split(",".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()).map { s: String? -> s!!.toInt() }.collect(Collectors.toList())
    }

    private fun performAbilitiesActions(
        gameStatus: GameStatus?,
        cardToResolve: CardInstance,
        abilities: List<Ability>
    ) {
        try {
            abilities.forEach(Consumer { ability: Ability? ->
                performAbilityAction(
                    gameStatus,
                    cardToResolve,
                    ability!!
                )
            })
        } catch (e: MessageException) {
            LOGGER.info(
                "{}: Target is now invalid during resolution, Cancelling the entire spell. [{}] ",
                cardToResolve.idAndName,
                e.message
            )
        }

        cardToResolve.modifiers.resetTargets()
    }

    private fun performAbilityAction(gameStatus: GameStatus?, cardToResolve: CardInstance, ability: Ability) {
        val abilityAction = abilityActionFactory.getAbilityAction(ability.abilityType)
        if (abilityAction != null) {
            checkTargets(gameStatus, cardToResolve, ability)
            abilityAction.perform(cardToResolve, gameStatus!!, ability)
        }
    }

    private fun checkTargets(gameStatus: GameStatus?, cardToResolve: CardInstance, ability: Ability) {
        for (i in ability.targets.indices) {
            val target = ability.targets.get(i)
            val targetId = targetCheckerService.getTargetIdAtIndex(cardToResolve, ability, i)
            targetCheckerService.check(gameStatus!!, cardToResolve, target, targetId)
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ResolveService::class.java)
    }
}
