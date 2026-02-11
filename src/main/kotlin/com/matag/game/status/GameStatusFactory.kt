package com.matag.game.status

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component

@Component
class GameStatusFactory : ApplicationContextAware {
    private var applicationContext: ApplicationContext? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    fun create(gameId: String?): GameStatus {
        val gameStatus = applicationContext!!.getBean(GameStatus::class.java)
        gameStatus.gameId = gameId
        return gameStatus
    }
}
