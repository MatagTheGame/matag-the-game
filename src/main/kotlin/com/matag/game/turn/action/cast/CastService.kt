package com.matag.game.turn.action.cast

import com.matag.cards.ability.trigger.TriggerSubtype
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.cost.CostService
import com.matag.game.cardinstance.cost.PayCostService
import com.matag.game.message.MessageException
import com.matag.game.player.Player
import com.matag.game.stack.SpellType
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.target.TargetCheckerService
import com.matag.game.turn.action.trigger.WhenTriggerService
import com.matag.game.turn.phases.PhaseUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class CastService(
    private val targetCheckerService: TargetCheckerService,
    private val manaCountService: ManaCountService,
    private val costService: CostService,
    private val payCostService: PayCostService,
    private val instantSpeedService: InstantSpeedService,
    private val whenTriggerService: WhenTriggerService
) {
    
    fun cast(
        gameStatus: GameStatus,
        cardId: Int,
        mana: Map<Int, List<String>>,
        targetsIdsForCardIds: Map<Int, List<Any>>,
        playedAbility: String?
    ) {
        val turn = gameStatus.turn
        val activePlayer = gameStatus.activePlayer

        val cardToCast: CardInstance
        val spellType: SpellType?
        if (activePlayer.hand.hasCardById(cardId)) {
            cardToCast = activePlayer.hand.findCardById(cardId)
            spellType = SpellType.SPELL
        } else {
            cardToCast = activePlayer.battlefield.findCardById(cardId)
            spellType = SpellType.ABILITY
        }

        if (!PhaseUtils.isMainPhase(turn.currentPhase!!) && !instantSpeedService.isAtInstantSpeed(
                cardToCast,
                playedAbility
            )
        ) {
            throw MessageException(cardToCast.idAndName + (if (playedAbility == null) "'s ability" else "") + " cannot be cast at instant speed.")
        } else {
            checkSpellOrAbilityCost(mana, activePlayer, cardToCast, playedAbility)
            targetCheckerService.checkSpellOrAbilityTargetRequisites(
                cardToCast,
                gameStatus,
                targetsIdsForCardIds,
                playedAbility
            )

            if (spellType == SpellType.SPELL) {
                activePlayer.hand.extractCardById(cardId)
                cardToCast.controller = activePlayer.name
                gameStatus.stack.push(cardToCast)
                whenTriggerService.whenTriggered(gameStatus, cardToCast, TriggerSubtype.WHEN_CAST)
            } else {
                val triggeredAbility = cardToCast.getAbilitiesByType(AbilityType.valueOf(playedAbility!!)).get(0)
                cardToCast.triggeredAbilities += triggeredAbility
                LOGGER.info(
                    "Player {} triggered ability {} for {}.",
                    activePlayer.name,
                    triggeredAbility.abilityType,
                    cardToCast.modifiers
                )
                gameStatus.stack.push(cardToCast)
            }

            payCostService.pay(gameStatus, activePlayer, cardToCast, playedAbility, mana)
            gameStatus.turn.passPriority(gameStatus)
        }
    }

    private fun checkSpellOrAbilityCost(
        mana: Map<Int, List<String>>,
        currentPlayer: Player,
        cardToCast: CardInstance,
        ability: String?
    ) {
        val paidCost = manaCountService.verifyManaPaid(mana, currentPlayer)
        if (!costService.isCastingCostFulfilled(cardToCast, ability, paidCost)) {
            throw MessageException("There was an error while paying the cost for " + cardToCast.idAndName + ".")
        }
    }


    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(CastService::class.java)
    }
}
