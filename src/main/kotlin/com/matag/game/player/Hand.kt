package com.matag.game.player

import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.cardinstance.CardListComponent

class Hand(private val cardInstanceFactory: CardInstanceFactory) : CardListComponent() {
    // TODO: move mask into CardInstance, remove the dependency to the Factory
    fun maskedHand(): List<CardInstance> {
        return cardInstanceFactory.mask(this.cards)
    }
}
