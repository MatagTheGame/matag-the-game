package com.aa.mtg.game.turn.action.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static java.lang.Integer.parseInt;

@Component
public class AttachAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachAction.class);

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        String target = cardInstance.getModifiers().getTargets().get(0).toString();
        int attachedToId = parseInt(target);
        cardInstance.getModifiers().setAttachedToId(attachedToId);
        LOGGER.info("attached {} to {}", cardInstance.getIdAndName(), target);
    }
}
