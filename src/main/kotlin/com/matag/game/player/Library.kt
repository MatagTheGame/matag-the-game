package com.matag.game.player

import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardListComponent

class Library : CardListComponent() {
    fun peek(n: Int): List<CardInstance> =
        cards.subList(0, n)

    fun draw(): CardInstance =
        cards.first().also {
            cards.drop(1)
        }

    fun shuffle() {
        cards = cards.shuffled()
    }
}
