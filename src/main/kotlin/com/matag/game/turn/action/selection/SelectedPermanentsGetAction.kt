package com.matag.game.turn.action.selection

import com.matag.cards.ability.Ability
import com.matag.cards.ability.AbilityAction
import com.matag.cards.ability.selector.SelectorType
import com.matag.game.cardinstance.CardInstance
import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.permanent.PermanentGetService
import com.matag.game.turn.action.player.PlayerGetService
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class SelectedPermanentsGetAction(
    private val magicInstancePermanentSelectorService: MagicInstancePermanentSelectorService,
    private val magicInstancePlayerSelectorService: MagicInstancePlayerSelectorService,
    private val permanentGetService: PermanentGetService,
    private val playerGetService: PlayerGetService
) : AbilityAction {
    
    override fun perform(cardInstance: CardInstance, gameStatus: GameStatus, ability: Ability?) {
        val magicInstanceSelector = ability!!.magicInstanceSelector
        if (magicInstanceSelector!!.selectorType == SelectorType.PLAYER) {
            magicInstancePlayerSelectorService.selectPlayers(gameStatus, cardInstance, magicInstanceSelector)
                .forEach(Consumer { player: Player? ->
                    playerGetService.thatPlayerGets(
                        cardInstance,
                        gameStatus,
                        ability.parameters,
                        player!!
                    )
                })
        } else {
            magicInstancePermanentSelectorService.select(gameStatus, cardInstance, magicInstanceSelector).cards
                .forEach(Consumer { card: CardInstance? ->
                    permanentGetService.thatPermanentGets(
                        cardInstance,
                        gameStatus,
                        ability.parameters,
                        card!!
                    )
                })
        }
    }
}
