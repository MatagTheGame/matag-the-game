package com.matag.game.player

import com.matag.game.cardinstance.CardInstance

data class PlayerUpdateEvent(
    var name: String? = null,
    var life: Int = 0,
    var librarySize: Int = 0,
    var visibleLibrary: List<CardInstance> = listOf(),
    var hand: List<CardInstance> = listOf(),
    var battlefield: List<CardInstance> = listOf(),
    var graveyard: List<CardInstance> = listOf()
)
