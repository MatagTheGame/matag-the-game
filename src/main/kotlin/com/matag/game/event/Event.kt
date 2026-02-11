package com.matag.game.event

import com.matag.game.player.Player

class Event @JvmOverloads constructor(val type: String?, val value: Any? = null) {
    constructor(type: String?, player: Player, value: Any?) : this(type, map(player.name, value))

    companion object {
        private fun map(player: String?, value: Any?): MutableMap<String?, Any?> {
            val map = HashMap<String?, Any?>()
            map.put("playerName", player)
            map.put("value", value)
            return map
        }
    }
}
