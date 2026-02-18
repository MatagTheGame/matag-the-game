package com.matag.game.player

import com.matag.adminentities.PlayerInfo
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.security.SecurityToken
import org.springframework.stereotype.Component

@Component
class PlayerFactory(
    private val cardInstanceFactory: CardInstanceFactory
) {
    fun create(token: SecurityToken, playerInfo: PlayerInfo): Player {
        return Player(
            library = Library(),
            hand = Hand(cardInstanceFactory),
            battlefield = Battlefield(),
            graveyard = Graveyard(),
            token = token,
            name = playerInfo.playerName
        )
    }
}
