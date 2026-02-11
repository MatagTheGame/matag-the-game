package com.matag.game.deck

import com.matag.adminentities.DeckInfo
import com.matag.cards.Card
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class DeckFactory {
    private val cardInstanceFactory: CardInstanceFactory? = null

    fun create(playerName: String?, gameStatus: GameStatus, deckInfo: DeckInfo): List<CardInstance> {
        return deckInfo.cards.stream()
            .map<CardInstance> { card: Card? ->
                cardInstanceFactory!!.create(
                    gameStatus,
                    gameStatus.nextCardId(),
                    card,
                    playerName
                )
            }
            .collect(Collectors.toList())
    }
}
