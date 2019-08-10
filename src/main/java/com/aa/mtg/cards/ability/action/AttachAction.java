package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static java.lang.Integer.parseInt;

@Service
public class AttachAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(AttachAction.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;

    public AttachAction(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        String target = cardInstance.getModifiers().getTargets().get(0).toString();
        int attachedToId = parseInt(target);
        cardInstance.getModifiers().setAttachedToId(attachedToId);
        LOGGER.info("attached {} to {}", cardInstance.getIdAndName(), target);
        gameStatusUpdaterService.sendUpdatePlayerBattlefield(gameStatus, gameStatus.getCurrentPlayer());
    }
}
