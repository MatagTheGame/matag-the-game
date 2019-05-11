package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ShuffleTargetGraveyardIntoLibraryAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShuffleTargetGraveyardIntoLibraryAction.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;

    @Autowired
    public ShuffleTargetGraveyardIntoLibraryAction(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus) {
        String targetPlayerName = (String)cardInstance.getModifiers().getTargets().get(0);
        Player targetPlayer = gameStatus.getPlayerByName(targetPlayerName);

        ArrayList<CardInstance> graveyardCards = targetPlayer.getGraveyard().extractAllCards();
        targetPlayer.getLibrary().addCards(graveyardCards);
        targetPlayer.getLibrary().shuffle();

        gameStatusUpdaterService.sendUpdatePlayerGraveyard(gameStatus, targetPlayer);
        gameStatusUpdaterService.sendUpdatePlayerLibrarySize(gameStatus, targetPlayer);

        LOGGER.info("{} drew shuffled graveyard into library.", targetPlayer.getName());
    }
}
