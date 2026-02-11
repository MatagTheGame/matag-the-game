package com.matag.game.turn.action.shuffle

import com.matag.cards.ability.Ability
import com.matag.cards.ability.AbilityAction
import com.matag.game.cardinstance.CardInstance
import com.matag.game.status.GameStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class ShuffleTargetGraveyardIntoLibraryAction : AbilityAction {
    override fun perform(cardInstance: CardInstance, gameStatus: GameStatus, ability: Ability?) {
        val targetPlayerName = cardInstance.modifiers.targets.get(0) as String?
        val targetPlayer = gameStatus.getPlayerByName(targetPlayerName)

        val graveyardCards = targetPlayer.graveyard.extractAllCards()
        targetPlayer.library.addCards(graveyardCards)
        targetPlayer.library.shuffle()

        LOGGER.info("{} drew shuffled graveyard into library.", targetPlayer.name)
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(ShuffleTargetGraveyardIntoLibraryAction::class.java)
    }
}
