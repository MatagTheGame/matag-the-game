package com.matag.game.turn

import com.matag.game.status.GameStatus
import com.matag.game.turn.action._continue.ContinueService
import com.matag.game.turn.action._continue.ResolveService
import com.matag.game.turn.action.cast.CastService
import com.matag.game.turn.action.cast.PlayLandService
import com.matag.game.turn.action.combat.DeclareAttackerService
import com.matag.game.turn.action.combat.DeclareBlockerService
import org.springframework.stereotype.Component

@Component
class TurnService(
    private val continueService: ContinueService,
    private val playLandService: PlayLandService,
    private val castService: CastService,
    private val resolveService: ResolveService,
    private val declareAttackerService: DeclareAttackerService,
    private val declareBlockerService: DeclareBlockerService
) {
    
    fun continueTurn(gameStatus: GameStatus) {
        continueService.next(gameStatus)
    }

    fun playLand(gameStatus: GameStatus, cardId: Int) {
        playLandService.playLand(gameStatus, cardId)
    }

    fun cast(
        gameStatus: GameStatus,
        cardId: Int,
        mana: Map<Int, List<String>>,
        targetsIdsForCardIds: Map<Int, List<Any>>,
        playedAbility: String?
    ) {
        castService.cast(gameStatus, cardId, mana, targetsIdsForCardIds, playedAbility)
    }

    fun resolve(
        gameStatus: GameStatus,
        inputRequiredAction: String,
        inputRequiredChoices: String?,
        targetCardIds: List<Int>,
        targetsIdsForCardIds: Map<Int, List<Any>>
    ) {
        resolveService.resolve(
            gameStatus,
            inputRequiredAction,
            inputRequiredChoices,
            targetCardIds,
            targetsIdsForCardIds
        )
    }

    fun declareAttackers(gameStatus: GameStatus, cardIds: List<Int>) {
        declareAttackerService.declareAttackers(gameStatus, cardIds)
    }

    fun declareBlockers(gameStatus: GameStatus, targetsIdsForCardIds: Map<Int, List<Int>>) {
        declareBlockerService.declareBlockers(gameStatus, targetsIdsForCardIds)
    }
}
