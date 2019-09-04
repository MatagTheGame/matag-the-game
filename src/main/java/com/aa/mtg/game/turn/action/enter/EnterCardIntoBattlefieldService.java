package com.aa.mtg.game.turn.action.enter;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.leave.DestroyPermanentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.aa.mtg.cards.ability.trigger.TriggerSubtype.WHEN_IT_ENTERS_THE_BATTLEFIELD;
import static com.aa.mtg.cards.ability.type.AbilityType.ENTERS_THE_BATTLEFIELD_TAPPED;
import static com.aa.mtg.cards.ability.type.AbilityType.HASTE;
import static com.aa.mtg.cards.properties.Type.CREATURE;

@Component
public class EnterCardIntoBattlefieldService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnterCardIntoBattlefieldService.class);

    private final DestroyPermanentService destroyPermanentService;

    @Autowired
    public EnterCardIntoBattlefieldService(DestroyPermanentService destroyPermanentService) {
        this.destroyPermanentService = destroyPermanentService;
    }

    public void enter(GameStatus gameStatus, CardInstance cardInstance) {
        String controller = cardInstance.getController();
        gameStatus.getPlayerByName(controller).getBattlefield().addCard(cardInstance);

        if (cardInstance.isOfType(CREATURE) && !cardInstance.hasAbility(HASTE)) {
            cardInstance.getModifiers().setSummoningSickness(true);
        }

        if (cardInstance.hasAbility(ENTERS_THE_BATTLEFIELD_TAPPED)) {
            cardInstance.getModifiers().tap();
        }

        for (Ability ability : cardInstance.getAbilities()) {
            if (ability.hasTriggerOfSubtype(WHEN_IT_ENTERS_THE_BATTLEFIELD)) {
                cardInstance.getTriggeredAbilities().add(ability);
                LOGGER.info("Event {} triggered with ability {} for {}.", WHEN_IT_ENTERS_THE_BATTLEFIELD, ability.getAbilityType(), cardInstance.getModifiers());
                gameStatus.getStack().add(cardInstance);
                return;
            }
        }

        destroyCreaturesWith0ToughnessOrLowerForPlayer(gameStatus, gameStatus.getCurrentPlayer());
        destroyCreaturesWith0ToughnessOrLowerForPlayer(gameStatus, gameStatus.getNonCurrentPlayer());
    }

    private void destroyCreaturesWith0ToughnessOrLowerForPlayer(GameStatus gameStatus, Player player) {
        player.getBattlefield().getCardsCopy().stream()
                .filter(cardInstance -> cardInstance.isOfType(CREATURE))
                .filter(cardInstance -> cardInstance.getToughness() <= 0)
                .forEach(cardInstance -> destroyPermanentService.destroy(gameStatus, cardInstance.getId()));
    }

}
