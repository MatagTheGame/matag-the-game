package com.matag.game.cardinstance.cost

import com.matag.game.cardinstance.CardInstance
import com.matag.game.player.Player
import com.matag.game.status.GameStatus
import com.matag.game.turn.action.tap.TapPermanentService
import org.springframework.stereotype.Component

@Component
class PayCostService {
    private val costService: CostService? = null
    private val tapPermanentService: TapPermanentService? = null

    fun pay(
        gameStatus: GameStatus,
        player: Player,
        cardToCast: CardInstance,
        playedAbility: String?,
        paidMana: Map<Int, List<String>>
    ) {
        // FIXME Antonio: Do not tap all lands but only the one necessary to pay the cost above. If not player may lose some mana if miscalculated.
        paidMana.keys.stream()
            .map<CardInstance?> { cardInstanceId: Int? -> player.battlefield.findCardById(cardInstanceId!!) }
            .forEach { card: CardInstance? -> tapPermanentService!!.tap(gameStatus, card!!.id) }
        gameStatus.turn.lastManaPaid = paidMana

        if (costService!!.needsTapping(cardToCast.card!!, playedAbility)) {
            tapPermanentService!!.tap(gameStatus, cardToCast.id)
        }
    }
}
