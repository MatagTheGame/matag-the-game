package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.search.CardSearch;
import com.aa.mtg.game.player.LifeService;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DealXDamageToTargetAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(DealXDamageToTargetAction.class);

    private final DestroyTargetAction destroyTargetAction;
    private final LifeService lifeService;

    @Autowired
    public DealXDamageToTargetAction(DestroyTargetAction destroyTargetAction, LifeService lifeService) {
        this.destroyTargetAction = destroyTargetAction;
        this.lifeService = lifeService;
    }

    @Override
    public void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus) {
        Object targetId = cardInstance.getModifiers().getTargets().get(0);
        int damageToDeal = Integer.valueOf(ability.getParameters().get(0));

        if (targetId instanceof String) {
            String targetPlayerName = (String) targetId;
            Player player = gameStatus.getPlayerByName(targetPlayerName);
            lifeService.substract(player, damageToDeal, gameStatus);

        } else {
            int targetCardId = (int) targetId;

            Optional<CardInstance> targetThatIsGettingDamageOptional = new CardSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                    .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards())
                    .withId(targetCardId);

            if (targetThatIsGettingDamageOptional.isPresent()) {
                CardInstance targetThatIsGettingDamage = targetThatIsGettingDamageOptional.get();

                dealDamageToCreature(gameStatus, targetThatIsGettingDamage, damageToDeal, false);
                LOGGER.info("AbilityActionExecuted: {} deals {}", cardInstance.getIdAndName(), targetCardId);

            } else {
                LOGGER.info("target {} is not anymore valid.", targetCardId);
            }
        }
    }

    public void dealDamageToCreature(GameStatus gameStatus, CardInstance cardInstance, int damage, boolean deathtouch) {
        LOGGER.info("{} is getting {} damage.", cardInstance.getIdAndName(), damage);
        cardInstance.getModifiers().dealDamage(damage);
        if (cardInstance.getModifiers().getDamage() >= cardInstance.getToughness() || damage > 0 && deathtouch) {
            destroyTargetAction.destroy(gameStatus, cardInstance.getId());
            LOGGER.info("{} has been destroyed.", cardInstance.getIdAndName());
        }
    }
}
