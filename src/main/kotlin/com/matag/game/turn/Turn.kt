package com.matag.game.turn

import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class Turn {
    var turnNumber = 0
    var currentTurnPlayer: String? = null
    var currentPhase: String? = null
    var currentPhaseActivePlayer: String? = null
    var cardsPlayedWithinTurn: List<CardInstance> = listOf()
    var lastManaPaid: Map<Int, List<String>> = mapOf()
    var inputRequiredAction: String? = null
    var inputRequiredActionParameter: String? = null
    var winner: String? = null

    fun init(playerName: String) {
        this.turnNumber = 1
        this.currentPhase = "UP"
        this.currentTurnPlayer = playerName
        this.currentPhaseActivePlayer = playerName
    }

    fun increaseTurnNumber() {
        turnNumber++
    }

    fun addCardToCardsPlayedWithinTurn(cardInstance: CardInstance) {
        cardsPlayedWithinTurn += cardInstance
    }

    fun isEnded(): Boolean =
        winner != null

    fun passPriority(gameStatus: GameStatus) {
        if (currentPhaseActivePlayer == gameStatus.player1!!.name) {
            currentPhaseActivePlayer = gameStatus.player2!!.name
        } else {
            currentPhaseActivePlayer = gameStatus.player1!!.name
        }
    }
}