package com.matag.game.init

import com.matag.game.status.GameStatus
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/** Prod does not allow to redefine the status of a game **/
@Component
@Profile("!test")
open class NoInitService : InitService {
    override fun initGameStatus(gameStatus: GameStatus) {

    }
}
