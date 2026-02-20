package com.matag.game.deck

import com.matag.adminentities.DeckInfo
import com.matag.cards.Card
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class DeckFactory(
    private val cardInstanceFactory: CardInstanceFactory
) {
    fun create(playerName: String?, gameStatus: GameStatus, deckInfo: DeckInfo): List<CardInstance> =
        deckInfo.cards.map { cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), it, playerName) }
}
