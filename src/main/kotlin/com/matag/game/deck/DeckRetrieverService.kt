package com.matag.game.deck

import com.matag.adminentities.DeckInfo
import com.matag.game.adminclient.AdminClient
import com.matag.game.security.SecurityToken
import org.springframework.stereotype.Component

@Component
class DeckRetrieverService(
    private val adminClient: AdminClient
) {
    fun retrieveDeckForUser(token: SecurityToken): DeckInfo =
        adminClient.getDeckInfo(token)
}
