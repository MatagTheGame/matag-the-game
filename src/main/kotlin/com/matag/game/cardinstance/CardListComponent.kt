package com.matag.game.cardinstance

abstract class CardListComponent {
    var cards: List<CardInstance> = listOf()

    fun search(): CardInstanceSearch {
        return CardInstanceSearch(cards)
    }

    fun size(): Int {
        return cards.size
    }

    fun addCard(cardInstance: CardInstance) {
        cards = cards + cardInstance
    }

    fun addCards(cardInstance: List<CardInstance>) {
        cards + cardInstance
    }

    fun hasCardById(cardId: Int): Boolean =
        search().withId(cardId) != null

    fun findCardById(cardId: Int): CardInstance =
        search().withId(cardId) ?: throw RuntimeException("Card with id $cardId not found.")

    fun extractCardsByIds(cardIds: List<Int>): List<CardInstance> =
        cardIds.map { findCardById(it) }
            .also { this.cards -= it }

    fun extractCardById(cardId: Int): CardInstance {
        return extractCardsByIds(listOf(cardId)).single()
    }

    fun extractAllCards(): List<CardInstance> =
        cards.also {
            cards = emptyList()
        }
}
