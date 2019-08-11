package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReturnTargetToHandService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReturnTargetToHandService.class);

    private GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public ReturnTargetToHandService(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    public void returnTargetToHand(GameStatus gameStatus, int targetId) {
        CardInstance cardToReturn = gameStatus.findCardByIdFromAnyBattlefield(targetId);
        if (cardToReturn != null) {
            Player owner = gameStatus.getPlayerByName(cardToReturn.getOwner());
            cardToReturn.cleanup();
            gameStatus.extractCardByIdFromAnyBattlefield(cardToReturn.getId());
            owner.getHand().addCard(cardToReturn);

            gameStatusUpdaterService.sendUpdatePlayerHand(gameStatus, owner);

        } else {
            LOGGER.info("target {} is not anymore valid.", targetId);
        }
    }
}
