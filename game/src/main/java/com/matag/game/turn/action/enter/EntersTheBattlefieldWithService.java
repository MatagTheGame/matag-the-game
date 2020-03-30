package com.matag.game.turn.action.enter;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.ability.CardInstanceAbility;
import com.matag.cards.ability.AbilityService;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.cast.ManaCountService;
import com.matag.game.turn.action.draw.DrawXCardsService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.matag.cards.ability.type.AbilityType.ADAMANT;
import static com.matag.cards.ability.type.AbilityType.ENTERS_THE_BATTLEFIELD_WITH;
import static java.util.stream.Collectors.toList;

@Component
@AllArgsConstructor
public class EntersTheBattlefieldWithService {
  private static final int ADAMANT_THRESHOLD = 3;

  private final ManaCountService manaCountService;
  private final DrawXCardsService drawXCardsService;
  private final AbilityService abilityService;

  void entersTheBattlefieldWith(GameStatus gameStatus, CardInstance cardInstance) {
    List<String> parameters = addEntersTheBattlefieldWithParameters(cardInstance);
    parameters.addAll(addAdamantEntersTheBattlefieldWithParameters(gameStatus, cardInstance));
    executeParameters(gameStatus, cardInstance, parameters);
  }

  private List<String> addEntersTheBattlefieldWithParameters(CardInstance cardInstance) {
    List<CardInstanceAbility> entersTheBattlefieldWith = cardInstance.getAbilitiesByType(ENTERS_THE_BATTLEFIELD_WITH);
    return entersTheBattlefieldWith.stream().map(CardInstanceAbility::getParameters).flatMap(Collection::stream).collect(toList());
  }

  private List<String> addAdamantEntersTheBattlefieldWithParameters(GameStatus gameStatus, CardInstance cardInstance) {
    List<String> parameters = new ArrayList<>();
    List<CardInstanceAbility> adamantAbilities = cardInstance.getAbilitiesByType(ADAMANT);

    for (CardInstanceAbility adamant : adamantAbilities) {
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
