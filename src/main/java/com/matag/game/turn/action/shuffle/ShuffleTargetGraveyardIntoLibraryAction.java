package com.matag.game.turn.action.shuffle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.cards.ability.AbilityAction;
import com.matag.cards.ability.Ability;
import com.matag.game.status.GameStatus;

@Component
public class ShuffleTargetGraveyardIntoLibraryAction implements AbilityAction {
  private static final Logger LOGGER = LoggerFactory.getLogger(ShuffleTargetGraveyardIntoLibraryAction.class);

  @Override
  public void perform(CardInstance cardInstance, GameStatus gameStatus, Ability ability) {
    var targetPlayerName = (String) cardInstance.getModifiers().getTargets().get(0);
    var targetPlayer = gameStatus.getPlayerByName(targetPlayerName);

    var graveyardCards = targetPlayer.getGraveyard().extractAllCards();
    targetPlayer.getLibrary().addCards(graveyardCards);
    targetPlayer.getLibrary().shuffle();

    LOGGER.info("{} drew shuffled graveyard into library.", targetPlayer.getName());
  }
}
