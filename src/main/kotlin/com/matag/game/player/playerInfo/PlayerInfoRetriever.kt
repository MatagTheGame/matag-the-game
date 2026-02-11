package com.matag.game.player.playerInfo

import com.matag.adminentities.PlayerInfo
import com.matag.game.adminclient.AdminClient
import com.matag.game.security.SecurityToken
import org.springframework.stereotype.Component

@Component
class PlayerInfoRetriever(
    private val adminClient: AdminClient
) {
    fun retrieve(token: SecurityToken?): PlayerInfo? {
        return adminClient.getPlayerInfo(token)
    }
}
