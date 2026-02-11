package com.matag.game.player

import com.matag.game.security.SecurityToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.stream.IntStream

@Component
@Scope("prototype")
data class Player @Autowired constructor(
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
