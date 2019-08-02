package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.modifiers.PowerToughness;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.player.LifeService;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.DealDamageToCreatureService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.aa.mtg.cards.ability.Abilities.abilitiesFromParameters;
import static com.aa.mtg.cards.ability.Abilities.damageFromParameters;
import static com.aa.mtg.cards.ability.Abilities.powerToughnessFromParameters;

@Service
public class ThatTargetsGetAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThatTargetsGetAction.class);

    private final LifeService lifeService;
    private final DealDamageToCreatureService dealDamageToCreatureService;

    @Autowired
    public ThatTargetsGetAction(LifeService lifeService, DealDamageToCreatureService dealDamageToCreatureService) {
        this.lifeService = lifeService;
        this.dealDamageToCreatureService = dealDamageToCreatureService;
    }

    @Override
    public void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus) {
        for (Object targetId : cardInstance.getModifiers().getTargets()) {
            if (targetId instanceof String) {
                String targetPlayerName = (String) targetId;
                Player player = gameStatus.getPlayerByName(targetPlayerName);
                int damage = damageFromParameters(ability.getParameters());
                if (damage > 0) {
                    lifeService.substract(player, damage, gameStatus);
                    LOGGER.info("AbilityActionExecuted: {} deals {} damage to {}", cardInstance.getIdAndName(), damage, player.getName());
                }

            } else {
                int targetCardId = (int) targetId;

                Optional<CardInstance> targetOptional = new CardInstanceSearch(gameStatus.getCurrentPlayer().getBattlefield().getCards())
                        .concat(gameStatus.getNonActivePlayer().getBattlefield().getCards())
                        .withId(targetCardId);

                if (targetOptional.isPresent()) {
                    CardInstance target = targetOptional.get();

                    PowerToughness powerToughness = powerToughnessFromParameters(ability.getParameters());
                    target.getModifiers().addExtraPowerToughnessUntilEndOfTurn(powerToughness);

                    List<Ability> abilities = abilitiesFromParameters(ability.getParameters());
                    target.getModifiers().getAbilitiesUntilEndOfTurn().addAll(abilities);

                    int damage = damageFromParameters(ability.getParameters());
                    dealDamageToCreatureService.dealDamageToCreature(gameStatus, target, damage, false);

                    LOGGER.info("AbilityActionExecuted: {} target {} which gets {}", cardInstance.getIdAndName(), targetCardId, ability.getParameters());

                } else {
                    LOGGER.info("target {} is not anymore valid.", targetCardId);
                }
            }
        }
    }
}
