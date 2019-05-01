package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DestroyTargetAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(DestroyTargetAction.class);

    @Override
    public void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus) {
        Integer targetId = cardInstance.getModifiers().getTargets().get(0);
        LOGGER.info("Executing ability action for {} with target {}.", cardInstance.getIdAndName(), targetId);
        destroy(gameStatus, targetId);
    }

    public void destroy(GameStatus gameStatus, int targetId) {
        if (gameStatus.getNonCurrentPlayer().getBattlefield().hasCardById(targetId)) {
            CardInstance destroyedCard = gameStatus.getNonCurrentPlayer().getBattlefield().extractCardById(targetId);
            gameStatus.putIntoGraveyard(destroyedCard);
            LOGGER.info("{} destroyed.", destroyedCard.getIdAndName());

        } else if (gameStatus.getCurrentPlayer().getBattlefield().hasCardById(targetId)) {
            CardInstance destroyedCard = gameStatus.getCurrentPlayer().getBattlefield().extractCardById(targetId);
            gameStatus.putIntoGraveyard(destroyedCard);
            LOGGER.info("{} destroyed.", destroyedCard.getIdAndName());

        } else {
            LOGGER.info("target {} is not anymore valid.", targetId);
        }
    }
}
