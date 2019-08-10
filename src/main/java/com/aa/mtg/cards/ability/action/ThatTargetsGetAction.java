package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.modifiers.PowerToughness;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.player.LifeService;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.DealDamageToCreatureService;
import com.aa.mtg.game.turn.action.DestroyTargetService;
import com.aa.mtg.game.turn.action.TapTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.aa.mtg.cards.ability.Abilities.*;

@Service
public class ThatTargetsGetAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThatTargetsGetAction.class);

    private final LifeService lifeService;
    private final DealDamageToCreatureService dealDamageToCreatureService;
    private final DestroyTargetService destroyTargetService;
    private final TapTargetService tapTargetService;

    @Autowired
    public ThatTargetsGetAction(LifeService lifeService, DealDamageToCreatureService dealDamageToCreatureService, DestroyTargetService destroyTargetService, TapTargetService tapTargetService) {
        this.lifeService = lifeService;
        this.dealDamageToCreatureService = dealDamageToCreatureService;
        this.destroyTargetService = destroyTargetService;
        this.tapTargetService = tapTargetService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        for (Object targetId : cardInstance.getModifiers().getTargets()) {
            if (targetId instanceof String) {
                String targetPlayerName = (String) targetId;
                Player player = gameStatus.getPlayerByName(targetPlayerName);
                int damage = damageFromParameter(parameter);
                if (damage > 0) {
                    lifeService.subtract(player, damage, gameStatus);
                    LOGGER.info("AbilityActionExecuted: {} deals {} damage to {}", cardInstance.getIdAndName(), damage, player.getName());
                }

            } else {
                int targetCardId = (int) targetId;

                Optional<CardInstance> targetOptional = new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                        .concat(gameStatus.getNonCurrentPlayer().getBattlefield().getCards())
                        .withId(targetCardId);

                if (targetOptional.isPresent()) {
                    CardInstance target = targetOptional.get();

                    PowerToughness powerToughness = powerToughnessFromParameter(parameter);
                    target.getModifiers().addExtraPowerToughnessUntilEndOfTurn(powerToughness);

                    Optional<Ability> ability = abilityFromParameter(parameter);
                    ability.ifPresent(value -> target.getModifiers().getAbilitiesUntilEndOfTurn().add(value));

                    int damage = damageFromParameter(parameter);
                    dealDamageToCreatureService.dealDamageToCreature(gameStatus, target, damage, false);

                    int controllerDamage = controllerDamageFromParameter(parameter);
                    lifeService.subtract(gameStatus.getPlayerByName(cardInstance.getController()), controllerDamage, gameStatus);

                    if (destroyedFromParameter(parameter)) {
                        destroyTargetService.destroy(gameStatus, targetCardId);
                    }

                    if (tappedDoesNotUntapNextTurn(parameter)) {
                        tapTargetService.tapDoesNotUntapNextTurn(gameStatus, targetCardId);
                    }

                    LOGGER.info("AbilityActionExecuted: {} target {} which gets {}", cardInstance.getIdAndName(), targetCardId, parameter);

                } else {
                    LOGGER.info("target {} is not anymore valid.", targetCardId);
                }
            }
        }
    }
}
