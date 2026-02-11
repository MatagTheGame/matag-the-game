package com.matag.game.turn.phases

import com.matag.game.status.GameStatus

interface Phase {
    val name: String

    fun action(gameStatus: GameStatus)

    fun getNextPhase(gameStatus: GameStatus): Phase

    fun set(gameStatus: GameStatus)

    fun next(gameStatus: GameStatus)
}
