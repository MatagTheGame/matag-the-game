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

import static com.aa.mtg.cards.ability.Abilities.abilitiesFromParameters;
import static com.aa.mtg.cards.ability.Abilities.powerToughnessFromParameters;
import static com.aa.mtg.cards.properties.Type.CREATURE;

@Service
public class CreaturesYouControlGetXUntilEndOfTurn implements AbilityAction {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreaturesYouControlGetXUntilEndOfTurn.class);

    private final GameStatusUpdaterService gameStatusUpdaterService;

    public CreaturesYouControlGetXUntilEndOfTurn(GameStatusUpdaterService gameStatusUpdaterService) {
        this.gameStatusUpdaterService = gameStatusUpdaterService;
    }

    @Override
    public void perform(Ability ability, CardInstance cardInstance, GameStatus gameStatus) {
        PowerToughness powerToughness = powerToughnessFromParameters(ability.getParameters());
        List<Ability> abilities = abilitiesFromParameters(ability.getParameters());

        String controllerString = cardInstance.getController();
        Player controller = gameStatus.getPlayerByName(controllerString);

        List<CardInstance> cards = new CardInstanceSearch(controller.getBattlefield().getCards()).ofType(CREATURE).getCards();
        for (CardInstance card : cards) {
            PowerToughness originalPowerToughness = card.getModifiers().getExtraPowerToughnessUntilEndOfTurn();
            card.getModifiers().setExtraPowerToughnessUntilEndOfTurn(originalPowerToughness.combine(powerToughness));
            card.getModifiers().getAbilitiesUntilEndOfTurn().addAll(abilities);
        }

        gameStatusUpdaterService.sendUpdatePlayerBattlefield(gameStatus, controller);
        LOGGER.info("creatures you ({}) control get: {}", controllerString, powerToughness);
    }
}
