package com.matag.game.player

import com.matag.adminentities.PlayerInfo
import com.matag.game.security.SecurityToken
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class PlayerFactory : ApplicationContextAware {
    private var applicationContext: ApplicationContext? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    fun create(token: SecurityToken, playerInfo: PlayerInfo): Player {
        val player = applicationContext!!.getBean<Player>(Player::class.java)
        player.token = token
        player.name = playerInfo.playerName
        return player
    }
}
