package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.modifiers.PowerToughness;
import com.aa.mtg.cards.search.CardInstanceSearch;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.aa.mtg.cards.ability.Abilities.abilityFromParameter;
import static com.aa.mtg.cards.ability.Abilities.powerToughnessFromParameter;
import static com.aa.mtg.cards.properties.Type.CREATURE;

@Service
public class CreaturesYouControlGetXUntilEndOfTurnAction implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreaturesYouControlGetXUntilEndOfTurnAction.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;

    public CreaturesYouControlGetXUntilEndOfTurnAction(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void perform(CardInstance cardInstance, GameStatus gameStatus, String parameter) {
        PowerToughness powerToughness = powerToughnessFromParameter(parameter);
        Optional<Ability> ability = abilityFromParameter(parameter);

        String controllerString = cardInstance.getController();
        Player controller = gameStatus.getPlayerByName(controllerString);

        List<CardInstance> cards = new CardInstanceSearch(controller.getBattlefield().getCards()).ofType(CREATURE).getCards();
        for (CardInstance card : cards) {
            card.getModifiers().addExtraPowerToughnessUntilEndOfTurn(powerToughness);
            ability.ifPresent(value -> card.getModifiers().getAbilitiesUntilEndOfTurn().add(value));
        }

        gameStatusUpdaterService.sendUpdatePlayerBattlefield(gameStatus, controller);
        LOGGER.info("creatures you ({}) control get: {}", controllerString, powerToughness);
    }
}
