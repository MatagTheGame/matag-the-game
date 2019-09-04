package com.aa.mtg.game.turn.action.target;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.draw.PlayerDrawXCardsService;
import com.aa.mtg.game.turn.action.life.LifeService;
import com.aa.mtg.game.turn.action.permanent.PermanentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.aa.mtg.cards.ability.Abilities.damageFromParameter;
import static com.aa.mtg.cards.ability.Abilities.drawFromParameter;

@Component
public class ThatTargetsGetAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThatTargetsGetAction.class);

    private final LifeService lifeService;
    private final PlayerDrawXCardsService playerDrawXCardsService;
    private final PermanentService permanentService;

    @Autowired
    public ThatTargetsGetAction(LifeService lifeService, PlayerDrawXCardsService playerDrawXCardsService, PermanentService permanentService) {
        this.lifeService = lifeService;
        this.playerDrawXCardsService = playerDrawXCardsService;
        this.permanentService = permanentService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        for (Object targetId : cardInstance.getModifiers().getTargets()) {
            if (targetId instanceof String) {
                String targetPlayerName = (String) targetId;
                thatTargetPlayerGet(cardInstance, gameStatus, parameter, targetPlayerName);

            } else {
                int targetCardId = (int) targetId;

                Optional<CardInstance> targetOptional = gameStatus.getAllBattlefieldCards().withId(targetCardId);

                if (targetOptional.isPresent()) {
                    CardInstance target = targetOptional.get();

                    permanentService.thatPermanentGets(cardInstance, gameStatus, parameter, target);

                } else {
                    LOGGER.info("target {} is not anymore valid.", targetCardId);
                }
            }
        }
    }

    private void thatTargetPlayerGet(CardInstance cardInstance, GameStatus gameStatus, String parameter, String targetPlayerName) {
        Player player = gameStatus.getPlayerByName(targetPlayerName);
        int damage = damageFromParameter(parameter);
        if (damage > 0) {
            lifeService.subtract(player, damage, gameStatus);
            LOGGER.info("AbilityActionExecuted: {} deals {} damage to {}", cardInstance.getIdAndName(), damage, player.getName());
        }

        int cardsToDraw = drawFromParameter(parameter);
        if (cardsToDraw > 0) {
            playerDrawXCardsService.drawXCards(player, cardsToDraw);
        }
    }
}
