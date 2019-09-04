package com.aa.mtg.game.turn.action.leave;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.attach.AttachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static com.aa.mtg.cards.properties.Type.ENCHANTMENT;

@Component
public class LeaveBattlefieldService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveBattlefieldService.class);

    private final AttachService attachService;
    private final DestroyPermanentService destroyPermanentService;

    @Lazy
    @Autowired
    public LeaveBattlefieldService(AttachService attachService, DestroyPermanentService destroyPermanentService) {
        this.attachService = attachService;
        this.destroyPermanentService = destroyPermanentService;
    }

    CardInstance leaveTheBattlefield(GameStatus gameStatus, int permanentId) {
        CardInstance cardInstance = gameStatus.extractCardByIdFromAnyBattlefield(permanentId);

        if (cardInstance != null) {
            cardInstance.resetAllModifiers();

            for (CardInstance attachedCard : cardInstance.getAttachedCards()) {
                if (attachedCard.isOfType(ENCHANTMENT)) {
                    destroyPermanentService.destroy(gameStatus, attachedCard.getId());

                } else if (attachedCard.isOfType(ARTIFACT)) {
                    attachService.detach(cardInstance, attachedCard);
                }
            }

            LOGGER.info("{} left the battlefield.", cardInstance.getIdAndName());

        } else {
            LOGGER.info("target {} is not anymore valid.", permanentId);
        }

        return cardInstance;
    }
}
