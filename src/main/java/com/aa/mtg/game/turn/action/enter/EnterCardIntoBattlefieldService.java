package com.aa.mtg.game.turn.action.enter;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.properties.Type.CREATURE;

@Component
public class EnterCardIntoBattlefieldService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterCardIntoBattlefieldService.class);

    private final EntersTheBattlefieldWithService entersTheBattlefieldWithService;
    private final WhenItEntersTheBattlefieldService whenItEntersTheBattlefieldService;

    @Autowired
    public EnterCardIntoBattlefieldService(
        EntersTheBattlefieldWithService entersTheBattlefieldWithService,
        WhenItEntersTheBattlefieldService whenItEntersTheBattlefieldService) {
        this.entersTheBattlefieldWithService = entersTheBattlefieldWithService;
        this.whenItEntersTheBattlefieldService = whenItEntersTheBattlefieldService;
    }

    public void enter(GameStatus gameStatus, CardInstance cardInstance) {
        String controller = cardInstance.getController();
        gameStatus.getPlayerByName(controller).getBattlefield().addCard(cardInstance);

        cardInstance.getModifiers().setPermanentId(gameStatus.nextCardId());

        if (cardInstance.isOfType(CREATURE)) {
            cardInstance.getModifiers().setSummoningSickness(true);
        }

        LOGGER.info(cardInstance.getIdAndName() + " entered the battlefield.");

        entersTheBattlefieldWithService.entersTheBattlefieldWith(gameStatus, cardInstance);
        whenItEntersTheBattlefieldService.whenItEntersTheBattlefield(gameStatus, cardInstance);
    }

}
