package com.matag.cards.ability

import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus

interface AbilityAction {
    fun perform(cardInstance: CardInstance, gameStatus: GameStatus, ability: Ability?)
}
