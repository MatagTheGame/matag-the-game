package com.matag.game.adminclient

import com.matag.adminentities.DeckInfo
import com.matag.adminentities.PlayerInfo
import com.matag.game.config.ConfigService
import com.matag.game.security.SecurityToken
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Profile("test")
@Component
class TestAdminClient(configService: ConfigService) : AdminClient(configService) {
    override fun getDeckInfo(token: SecurityToken?): DeckInfo {
        return DeckInfo(listOf())
    }

    override fun getPlayerInfo(token: SecurityToken?): PlayerInfo {
        if (!playerJoined) {
            playerJoined = true
            return PlayerInfo("Guest-1")
        } else {
            return PlayerInfo("Guest-2")
        }
    }

    companion object {
        var playerJoined: Boolean = false
    }
}
