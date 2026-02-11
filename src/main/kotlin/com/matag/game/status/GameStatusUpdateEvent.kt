package com.matag.game.status

import com.matag.game.cardinstance.CardInstance
import com.matag.game.player.PlayerUpdateEvent
import com.matag.game.turn.Turn
import java.util.*

data class GameStatusUpdateEvent(
    var turn: Turn? = null,
    var stack: LinkedList<CardInstance> = LinkedList(),
    var playersUpdateEvents: Set<PlayerUpdateEvent> = setOf()
)
