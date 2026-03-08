package com.matag.game.init

import com.matag.game.status.GameStatus

/** Used by tests to setup a game in a particular state **/
interface InitService {
    fun initGameStatus(gameStatus: GameStatus)
}
