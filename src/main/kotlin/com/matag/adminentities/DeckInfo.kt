package com.matag.adminentities

import com.matag.cards.Card

data class DeckInfo(
    var cards: List<Card> = listOf()
)
