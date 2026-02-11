package com.matag.game.cardinstance

import com.matag.cards.Card
import com.matag.cards.ability.Ability
import com.matag.cards.properties.*
import com.matag.game.status.GameStatus
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class CardInstanceFactory : ApplicationContextAware {
    private var applicationContext: ApplicationContext? = null

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    @JvmOverloads
    fun create(
        gameStatus: GameStatus?,
        id: Int,
        card: Card?,
        owner: String?,
        controller: String? = null
    ): CardInstance {
        val cardInstance = applicationContext!!.getBean<CardInstance>(CardInstance::class.java)
        cardInstance.gameStatus = gameStatus
        cardInstance.id = id
        cardInstance.card = card
        cardInstance.owner = owner
        cardInstance.controller = controller
        return cardInstance
    }

    fun mask(cardInstance: CardInstance): CardInstance {
        return create(cardInstance.gameStatus, cardInstance.id, hiddenCard(), cardInstance.owner)
    }

    fun mask(cardInstanceList: List<CardInstance>): List<CardInstance> {
        return cardInstanceList
            .map { cardInstance: CardInstance? -> this.mask(cardInstance!!) }
    }

    private fun hiddenCard(): Card {
        return Card(
            "card",
            "/img/card-back.jpg",
            TreeSet(),
            listOf(),
            TreeSet(),
            TreeSet(),
            Rarity.COMMON,
            "",
            0,
            0,
            listOf()
        )
    }
}
