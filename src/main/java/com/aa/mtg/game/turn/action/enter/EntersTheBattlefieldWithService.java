package com.aa.mtg.game.turn.action.enter;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.cast.ManaCountService;
import com.aa.mtg.game.turn.action.draw.DrawXCardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.aa.mtg.cards.ability.Abilities.*;
import static com.aa.mtg.cards.ability.type.AbilityType.ADAMANT;
import static com.aa.mtg.cards.ability.type.AbilityType.ENTERS_THE_BATTLEFIELD_WITH;

@Component
public class EntersTheBattlefieldWithService {

    private static final int ADAMANT_THRESHOLD = 3;

    private final ManaCountService manaCountService;
    private final DrawXCardsService drawXCardsService;

    @Autowired
    public EntersTheBattlefieldWithService(ManaCountService manaCountService, DrawXCardsService drawXCardsService) {
        this.manaCountService = manaCountService;
        this.drawXCardsService = drawXCardsService;
    }

    void entersTheBattlefieldWith(GameStatus gameStatus, CardInstance cardInstance) {
        List<String> parameters = new ArrayList<>();
        addEntersTheBattlefieldWithParameters(cardInstance, parameters);
        addAdamantEntersTheBattlefieldWithParameters(gameStatus, cardInstance, parameters);
        executeParameters(gameStatus, cardInstance, parameters);
    }

    private void addEntersTheBattlefieldWithParameters(CardInstance cardInstance, List<String> parameters) {
        Optional<Ability> entersTheBattlefieldWith = cardInstance.getAbilityByType(ENTERS_THE_BATTLEFIELD_WITH);
        entersTheBattlefieldWith.map(Ability::getParameters).ifPresent(parameters::addAll);
    }

    private void addAdamantEntersTheBattlefieldWithParameters(GameStatus gameStatus, CardInstance cardInstance, List<String> parameters) {
        Optional<Ability> adamant = cardInstance.getAbilityByType(ADAMANT);

        if (adamant.isPresent()) {
            Map<Integer, List<String>> manaPaid = gameStatus.getTurn().getLastManaPaid();
            Map<String, Integer> manaPaidByColor = manaCountService.countManaPaid(manaPaid);
            String adamantColor = adamant.get().getParameter(0);
            boolean adamantFulfilled = isAdamantFulfilled(manaPaidByColor, adamantColor);

            if (adamantFulfilled) {
                adamant.map(Ability::getAbility).map(Ability::getParameters).ifPresent(parameters::addAll);
            }
        }
    }

    private boolean isAdamantFulfilled(Map<String, Integer> manaPaidByColor, String adamantColor) {
        if (adamantColor.equals("SAME") && manaPaidByColor.values().stream().anyMatch(key -> key >= ADAMANT_THRESHOLD)) {
            return true;

        } else if (manaPaidByColor.containsKey(adamantColor) && manaPaidByColor.get(adamantColor) >= ADAMANT_THRESHOLD) {
            return true;

        } else {
            return false;
        }
    }

    private void executeParameters(GameStatus gameStatus, CardInstance cardInstance, List<String> parameters) {
        for (String parameter : parameters) {
            if (tappedFromParameter(parameter)) {
                cardInstance.getModifiers().tap();
            }

            int plus1Counters = plus1CountersFromParameter(parameter);
            cardInstance.getModifiers().getCounters().addPlus1Counters(plus1Counters);

            int cardsToDraw = drawFromParameter(parameter);
            drawXCardsService.drawXCards(gameStatus.getPlayerByName(cardInstance.getController()), cardsToDraw);
        }
    }

}
