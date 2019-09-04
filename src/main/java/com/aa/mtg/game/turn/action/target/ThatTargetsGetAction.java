package com.aa.mtg.game.turn.action.target;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.action.AbilityAction;
import com.aa.mtg.cards.modifiers.PowerToughness;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action._return.ReturnTargetToHandService;
import com.aa.mtg.game.turn.action.damage.DealDamageToCreatureService;
import com.aa.mtg.game.turn.action.destroy.DestroyTargetService;
import com.aa.mtg.game.turn.action.draw.PlayerDrawXCardsService;
import com.aa.mtg.game.turn.action.life.LifeService;
import com.aa.mtg.game.turn.action.tap.TapTargetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static com.aa.mtg.cards.ability.Abilities.*;

@Component
public class ThatTargetsGetAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThatTargetsGetAction.class);

    private final LifeService lifeService;
    private final DealDamageToCreatureService dealDamageToCreatureService;
    private final DestroyTargetService destroyTargetService;
    private final TapTargetService tapTargetService;
    private final ReturnTargetToHandService returnTargetToHandService;
    private final PlayerDrawXCardsService playerDrawXCardsService;

    @Autowired
    public ThatTargetsGetAction(LifeService lifeService, DealDamageToCreatureService dealDamageToCreatureService, DestroyTargetService destroyTargetService, TapTargetService tapTargetService, ReturnTargetToHandService returnTargetToHandService, PlayerDrawXCardsService playerDrawXCardsService) {
        this.lifeService = lifeService;
        this.dealDamageToCreatureService = dealDamageToCreatureService;
        this.destroyTargetService = destroyTargetService;
        this.tapTargetService = tapTargetService;
        this.returnTargetToHandService = returnTargetToHandService;
        this.playerDrawXCardsService = playerDrawXCardsService;
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

                    thatTargetPermanentGet(cardInstance, gameStatus, parameter, target);

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

    public void thatTargetPermanentGet(CardInstance cardInstance, GameStatus gameStatus, String parameter, CardInstance target) {
        PowerToughness powerToughness = powerToughnessFromParameter(parameter);
        target.getModifiers().addExtraPowerToughnessUntilEndOfTurn(powerToughness);

        Optional<Ability> ability = abilityFromParameter(parameter);
        ability.ifPresent(value -> target.getModifiers().getAbilitiesUntilEndOfTurn().add(value));

        int damage = damageFromParameter(parameter);
        dealDamageToCreatureService.dealDamageToCreature(gameStatus, target, damage, false);

        int controllerDamage = controllerDamageFromParameter(parameter);
        lifeService.subtract(gameStatus.getPlayerByName(cardInstance.getController()), controllerDamage, gameStatus);

        if (destroyedFromParameter(parameter)) {
            destroyTargetService.destroy(gameStatus, target.getId());
        }

        if (tappedDoesNotUntapNextTurnFromParameter(parameter)) {
            tapTargetService.tapDoesNotUntapNextTurn(gameStatus, target.getId());
        }

        if (returnToOwnerHandFromParameter(parameter)) {
            returnTargetToHandService.returnTargetToHand(gameStatus, target.getId());
        }

        LOGGER.info("AbilityActionExecuted: {} target {} which gets {}", cardInstance.getIdAndName(), target.getId(), parameter);
    }
}
