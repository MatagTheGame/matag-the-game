package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static java.lang.Integer.parseInt;

@Service
public class Attach implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(Attach.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;

    public Attach(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus) {
        String target = cardInstance.getModifiers().getTargets().get(0).toString();
        cardInstance.getModifiers().setAttachedToId(parseInt(target));
        gameStatusUpdaterService.sendUpdateCurrentPlayerBattlefield(gameStatus);
        LOGGER.info("attached {} to {}", cardInstance.getIdAndName(), target);
    }
}
