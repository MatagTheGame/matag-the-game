package com.aa.mtg.game.turn.action.destroy;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static com.aa.mtg.cards.properties.Type.ENCHANTMENT;

@Component
public class DestroyTargetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DestroyTargetService.class);

    public void destroy(GameStatus gameStatus, int targetId) {
        CardInstance cardToDestroy = gameStatus.extractCardByIdFromAnyBattlefield(targetId);
        if (cardToDestroy != null) {
            for (CardInstance attachedCard : cardToDestroy.getAttachedCards()) {
                if (attachedCard.isOfType(ENCHANTMENT)) {
                    gameStatus.extractCardByIdFromAnyBattlefield(attachedCard.getId());
                    gameStatus.putIntoGraveyard(attachedCard);

                } else if (attachedCard.isOfType(ARTIFACT)) {
                    attachedCard.getModifiers().unsetAttachedId();
                }
            }

            gameStatus.putIntoGraveyard(cardToDestroy);
            LOGGER.info("{} destroyed.", cardToDestroy.getIdAndName());

        } else {
            LOGGER.info("target {} is not anymore valid.", targetId);
        }
    }
}
