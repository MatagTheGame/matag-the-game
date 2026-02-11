package com.matag.game.init

data class InitPlayerEvent(
    private val name: String? = null,
    private val gameConfig: GameConfig? = null
)
