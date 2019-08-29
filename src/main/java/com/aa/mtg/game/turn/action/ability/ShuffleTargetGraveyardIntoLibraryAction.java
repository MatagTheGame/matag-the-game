package com.aa.mtg.game.turn.action.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class ShuffleTargetGraveyardIntoLibraryAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ShuffleTargetGraveyardIntoLibraryAction.class);

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        String targetPlayerName = (String)cardInstance.getModifiers().getTargets().get(0);
        Player targetPlayer = gameStatus.getPlayerByName(targetPlayerName);

        ArrayList<CardInstance> graveyardCards = targetPlayer.getGraveyard().extractAllCards();
        targetPlayer.getLibrary().addCards(graveyardCards);
        targetPlayer.getLibrary().shuffle();

        LOGGER.info("{} drew shuffled graveyard into library.", targetPlayer.getName());
    }
}
