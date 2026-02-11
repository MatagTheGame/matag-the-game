package com.matag.game.stack

import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceSearch
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.*

@Component
@Scope("prototype")
class SpellStack {
    @JvmField
    var items: LinkedList<CardInstance> = LinkedList()

    fun isEmpty(): Boolean =
        items.isEmpty()

    fun push(cardInstance: CardInstance) {
        items.addLast(cardInstance)
    }

    fun peek(): CardInstance? {
        return items.peekLast()
    }

    fun pop(): CardInstance {
        return items.removeLast()
    }

    fun remove(card: CardInstance?) {
        items.remove(card)
    }

    fun search(): CardInstanceSearch {
        return CardInstanceSearch(items)
    }
}
