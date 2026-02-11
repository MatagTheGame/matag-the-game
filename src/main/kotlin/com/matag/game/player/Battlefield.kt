package com.matag.game.player

import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceSearch
import com.matag.game.cardinstance.CardListComponent
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.function.Consumer
import java.util.stream.Collectors

@Component
@Scope("prototype")
class Battlefield : CardListComponent() {
    fun removeAttacking() {
        CardInstanceSearch(cards).attacking().cards
            .forEach(Consumer { cardInstance: CardInstance? -> cardInstance!!.modifiers.isAttacking = false })
    }

    fun removeBlocking() {
        CardInstanceSearch(cards).blocking().cards
            .forEach(Consumer { cardInstance: CardInstance? -> cardInstance!!.modifiers.unsetBlockingCardId() })
    }

    val attackingCreatures: List<CardInstance>
        get() = CardInstanceSearch(cards).attacking().cards

    val blockingCreatures: List<CardInstance>
        get() = CardInstanceSearch(cards).blocking().cards

    fun getBlockingCreaturesFor(attackingCardId: Int): List<CardInstance> {
        return this.blockingCreatures
            .filter { it.modifiers.blockingCardId == attackingCardId }
    }
}
