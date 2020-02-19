package com.aa.mtg.game.turn.action.enter;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.AbilityService;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.cast.ManaCountService;
import com.aa.mtg.game.turn.action.draw.DrawXCardsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.ability.type.AbilityType.ADAMANT;
import static com.aa.mtg.cards.ability.type.AbilityType.ENTERS_THE_BATTLEFIELD_WITH;
import static java.util.stream.Collectors.toList;

@Component
public class EntersTheBattlefieldWithService {

    private static final int ADAMANT_THRESHOLD = 3;

    private final ManaCountService manaCountService;
    private final DrawXCardsService drawXCardsService;
    private final AbilityService abilityService;

    @Autowired
    public EntersTheBattlefieldWithService(ManaCountService manaCountService, DrawXCardsService drawXCardsService, AbilityService abilityService) {
        this.manaCountService = manaCountService;
        this.drawXCardsService = drawXCardsService;
        this.abilityService = abilityService;
    }

    void entersTheBattlefieldWith(GameStatus gameStatus, CardInstance cardInstance) {
        List<String> parameters = addEntersTheBattlefieldWithParameters(cardInstance);
        parameters.addAll(addAdamantEntersTheBattlefieldWithParameters(gameStatus, cardInstance));
        executeParameters(gameStatus, cardInstance, parameters);
    }

    private List<String> addEntersTheBattlefieldWithParameters(CardInstance cardInstance) {
        List<Ability> entersTheBattlefieldWith = cardInstance.getAbilitiesByType(ENTERS_THE_BATTLEFIELD_WITH);
        return entersTheBattlefieldWith.stream().map(Ability::getParameters).flatMap(Collection::stream).collect(toList());
    }

    private List<String> addAdamantEntersTheBattlefieldWithParameters(GameStatus gameStatus, CardInstance cardInstance) {
        List<String> parameters = new ArrayList<>();
        List<Ability> adamantAbilities = cardInstance.getAbilitiesByType(ADAMANT);

        for (Ability adamant : adamantAbilities) {
            Map<Integer, List<String>> manaPaid = gameStatus.getTurn().getLastManaPaid();
            Map<String, Integer> manaPaidByColor = manaCountService.countManaPaid(manaPaid);
            String adamantColor = adamant.getParameter(0);
            boolean adamantFulfilled = isAdamantFulfilled(manaPaidByColor, adamantColor);

            if (adamantFulfilled) {
                parameters.addAll(adamant.getAbility().getParameters());
            }
        }

        return parameters;
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
            if (abilityService.tappedFromParameter(parameter)) {
                cardInstance.getModifiers().tap();
            }

            int plus1Counters = abilityService.plus1CountersFromParameter(parameter);
            cardInstance.getModifiers().getCounters().addPlus1Counters(plus1Counters);

            int cardsToDraw = abilityService.drawFromParameter(parameter);
            drawXCardsService.drawXCards(gameStatus.getPlayerByName(cardInstance.getController()), cardsToDraw);
        }
    }

}
