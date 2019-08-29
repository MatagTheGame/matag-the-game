package com.aa.mtg.game.turn.action.service;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ReturnTargetToHandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReturnTargetToHandService.class);

    public void returnTargetToHand(GameStatus gameStatus, int targetId) {
        CardInstance cardToReturn = gameStatus.findCardByIdFromAnyBattlefield(targetId);
        if (cardToReturn != null) {
            Player owner = gameStatus.getPlayerByName(cardToReturn.getOwner());
            cardToReturn.resetAllModifiers();
            gameStatus.extractCardByIdFromAnyBattlefield(cardToReturn.getId());
            owner.getHand().addCard(cardToReturn);

        } else {
            LOGGER.info("target {} is not anymore valid.", targetId);
        }
    }
}
