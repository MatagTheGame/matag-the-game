package com.matag.game.player

import com.matag.game.security.SecurityToken

data class Player(
    val library: Library,
    val hand: Hand,
    val battlefield: Battlefield,
    val graveyard: Graveyard,
    var token: SecurityToken,
    var name: String? = null,
    var life: Int = 20,
    val resolution: String? = null
) {
    fun drawHand() =
        repeat(7) {
            hand.addCard(library.draw())
        }

    fun addLife(life: Int) {
        this.life += life
    }
}
