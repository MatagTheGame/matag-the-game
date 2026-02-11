package com.matag.game.player

import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.cardinstance.CardListComponent
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("prototype")
class Hand(private val cardInstanceFactory: CardInstanceFactory) : CardListComponent() {
    fun maskedHand(): List<CardInstance> {
        return cardInstanceFactory.mask(this.cards)
    }
}
