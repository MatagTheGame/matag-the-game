package com.matag.game.status

import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceSearch
import com.matag.game.player.Player
import com.matag.game.stack.SpellStack
import com.matag.game.turn.Turn
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicInteger

@Component
@Scope("prototype")
class GameStatus(val turn: Turn, val stack: SpellStack) {
    private val nextCardId = AtomicInteger()
    var gameId: String? = null
    var player1: Player? = null
    var player2: Player? = null

    fun getPlayerByName(name: String?): Player {
        if (this.player1?.name.equals(name)) {
            return player1!!
        } else {
            return player2!!
        }
    }

    fun getOtherPlayer(player: Player): Player {
        return getOtherPlayer(player.name)
    }

    fun getOtherPlayer(playerName: String?): Player {
        if (player1?.name.equals(playerName)) {
            return player2!!
        } else {
            return player1!!
        }
    }

    val currentPlayer: Player
        get() {
            if (turn.currentTurnPlayer == player1?.name) {
                return player1!!
            } else {
                return player2!!
            }
        }

    val nonCurrentPlayer: Player
        get() = getOtherPlayer(this.currentPlayer)

    val activePlayer: Player
        get() {
            if (turn.currentPhaseActivePlayer == this.player1?.name) {
                return player1!!
            } else {
                return player2!!
            }
        }

    val nonActivePlayer: Player?
        get() = getOtherPlayer(this.activePlayer)

    fun nextCardId(): Int {
        return nextCardId.incrementAndGet()
    }

    fun extractCardByIdFromAnyBattlefield(id: Int): CardInstance? {
        if (this.nonCurrentPlayer.battlefield.hasCardById(id)) {
            return this.nonCurrentPlayer.battlefield.extractCardById(id)
        } else if (this.currentPlayer.battlefield.hasCardById(id)) {
            return this.currentPlayer.battlefield.extractCardById(id)
        }

        return null
    }

    fun findCardByIdFromAnyBattlefield(id: Int): CardInstance? {
        if (this.nonCurrentPlayer.battlefield.hasCardById(id)) {
            return this.nonCurrentPlayer.battlefield.findCardById(id)
        } else if (this.currentPlayer.battlefield.hasCardById(id)) {
            return this.currentPlayer.battlefield.findCardById(id)
        }

        return null
    }

    val allBattlefieldCardsSearch: CardInstanceSearch
        get() = CardInstanceSearch(player1!!.battlefield.cards).concat(player2!!.battlefield.cards)

    val allBattlefieldCards: List<CardInstance>
        get() = this.allBattlefieldCardsSearch.cards

    val isCurrentPlayerActive: Boolean
        get() = turn.currentPhaseActivePlayer == this.currentPlayer.name
}
