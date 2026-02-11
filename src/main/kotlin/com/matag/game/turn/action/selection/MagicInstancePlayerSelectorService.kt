package com.matag.game.turn.action.selection

import com.matag.cards.ability.selector.MagicInstanceSelector
import com.matag.cards.ability.selector.SelectorType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.player.PlayerType
import org.springframework.stereotype.Component

@Component
class MagicInstancePlayerSelectorService {
    fun selectPlayers(
        gameStatus: GameStatus,
        cardInstance: CardInstance,
        magicInstanceSelector: MagicInstanceSelector
    ): MutableList<Player?> {
        val players = ArrayList<Player?>()

        if (magicInstanceSelector.selectorType == SelectorType.PLAYER) {
            if (magicInstanceSelector.itself) {
                players.add(gameStatus.getPlayerByName(cardInstance.controller))
            } else {
                val player = gameStatus.getPlayerByName(cardInstance.controller)
                val opponent = gameStatus.getOtherPlayer(player)
                players.add(opponent)
                if (magicInstanceSelector.controllerType != PlayerType.OPPONENT) {
                    players.add(player)
                }
            }
        }

        return players
    }
}
