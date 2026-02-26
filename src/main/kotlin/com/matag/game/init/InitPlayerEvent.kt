package com.matag.game.init

data class InitPlayerEvent(
    val name: String? = null,
    val gameConfig: GameConfig? = null
)
