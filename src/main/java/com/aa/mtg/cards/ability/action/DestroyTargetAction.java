package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DestroyTargetAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(DestroyTargetAction.class);

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus) {
        Integer targetId = cardInstance.getModifiers().getTargets().get(0);
        gameStatus.destroy(targetId);
        LOGGER.info("AbilityActionExecuted: " + cardInstance.getIdAndName() + " destroys " + targetId);
    }
}
