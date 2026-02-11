package com.matag.game.turn.action.permanent

import com.matag.cards.ability.AbilityService
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.counters.CountersService
import com.matag.game.turn.action.damage.DealDamageToCreatureService
import com.matag.game.turn.action.damage.DealDamageToPlayerService
import com.matag.game.turn.action.leave.DestroyPermanentService
import com.matag.game.turn.action.leave.PutIntoGraveyardService
import com.matag.game.turn.action.leave.ReturnPermanentToHandService
import com.matag.game.turn.action.tap.TapPermanentService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class PermanentGetService(
    private val dealDamageToCreatureService: DealDamageToCreatureService,
    private val dealDamageToPlayerService: DealDamageToPlayerService,
    private val destroyPermanentService: DestroyPermanentService,
    private val tapPermanentService: TapPermanentService,
    private val putIntoGraveyardService: PutIntoGraveyardService,
    private val returnPermanentToHandService: ReturnPermanentToHandService,
    private val gainControlPermanentService: GainControlPermanentService,
    private val countersService: CountersService,
    private val abilityService: AbilityService,
    private val cardInstanceAbilityFactory: CardInstanceAbilityFactory,
) {


    fun thatPermanentGets(
        cardInstance: CardInstance,
        gameStatus: GameStatus,
        parameters: List<String>,
        target: CardInstance
    ) {
        parameters.forEach(Consumer { parameter: String? ->
            thatPermanentGets(
                cardInstance,
                gameStatus,
                parameter!!,
                target
            )
        })
    }

    private fun thatPermanentGets(
        cardInstance: CardInstance,
        gameStatus: GameStatus,
        parameter: String,
        target: CardInstance
    ) {
        val PowerToughness = abilityService.powerToughnessFromParameter(parameter)
        target.modifiers.modifiersUntilEndOfTurn.addExtraPowerToughnessUntilEndOfTurn(PowerToughness)

        val ability = cardInstanceAbilityFactory.abilityFromParameter(parameter)?.apply {
            target.modifiers.modifiersUntilEndOfTurn.extraAbilities += this
        }

        val damage = abilityService.damageFromParameter(parameter)
        dealDamageToCreatureService.dealDamageToCreature(gameStatus, target, damage, false, cardInstance)

        val controllerDamage = abilityService.controllerDamageFromParameter(parameter)
        dealDamageToPlayerService.dealDamageToPlayer(
            gameStatus,
            controllerDamage,
            gameStatus.getPlayerByName(target.controller)
        )

        if (abilityService.destroyedFromParameter(parameter)) {
            destroyPermanentService.markToBeDestroyed(gameStatus, target.id)
        }

        if (abilityService.tappedFromParameter(parameter)) {
            tapPermanentService.tap(gameStatus, target.id)
        }

        if (abilityService.doesNotUntapNextTurnFromParameter(parameter)) {
            tapPermanentService.doesNotUntapNextTurn(gameStatus, target.id)
        }

        if (abilityService.untappedFromParameter(parameter)) {
            tapPermanentService!!.untap(gameStatus, target.id)
        }

        if (abilityService.cancelledFromParameter(parameter)) {
            val cardToCancel = gameStatus.stack.search().withId(target.id)?.apply {
                gameStatus.stack.remove(this)
                putIntoGraveyardService.putIntoGraveyard(gameStatus, this)
            }
        }

        if (abilityService.returnToOwnerHandFromParameter(parameter)) {
            returnPermanentToHandService.markAsToBeReturnedToHand(gameStatus, target.id)
        }

        if (abilityService.controlledFromParameter(parameter)) {
            gainControlPermanentService.gainControlUntilEndOfTurn(target, cardInstance.controller)
        }

        val plusCounters = abilityService.plus1CountersFromParameter(parameter)
        countersService.addPlus1Counters(target, plusCounters)

        val minusCounters = abilityService.minus1CountersFromParameter(parameter)
        countersService.addMinus1Counters(target, minusCounters)

        val keywordCounter = abilityService.keywordCounterFromParameter(parameter)
        if (keywordCounter != null) {
            countersService.addKeywordCounter(target, keywordCounter)
        }

        LOGGER.info("PermanentService: {} target {} which gets {}", cardInstance.idAndName, target.idAndName, parameter)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PermanentGetService::class.java)
    }
}
