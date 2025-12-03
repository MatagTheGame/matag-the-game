package com.matag.game.turn.action.enter;

import static com.matag.cards.ability.type.AbilityType.ADAMANT;
import static com.matag.cards.ability.type.AbilityType.ENTERS_THE_BATTLEFIELD_WITH;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.matag.cards.ability.Ability;
import org.springframework.stereotype.Component;

import com.matag.cards.ability.AbilityService;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.cast.ManaCountService;
import com.matag.game.turn.action.player.DrawXCardsService;
import com.matag.game.turn.action.tap.TapPermanentService;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EntersTheBattlefieldWithService {
  private static final int ADAMANT_THRESHOLD = 3;

  private final ManaCountService manaCountService;
  private final DrawXCardsService drawXCardsService;
  private final AbilityService abilityService;
  private final TapPermanentService tapPermanentService;

  void entersTheBattlefieldWith(GameStatus gameStatus, CardInstance cardInstance) {
    var parameters = addEntersTheBattlefieldWithParameters(cardInstance);
    parameters.addAll(addAdamantEntersTheBattlefieldWithParameters(gameStatus, cardInstance));
    executeParameters(gameStatus, cardInstance, parameters);
  }

  private List<String> addEntersTheBattlefieldWithParameters(CardInstance cardInstance) {
    var entersTheBattlefieldWith = cardInstance.getAbilitiesByType(ENTERS_THE_BATTLEFIELD_WITH);
    return entersTheBattlefieldWith.stream()
            .map(CardInstanceAbility::getAbility)
            .map(Ability::getParameters)
            .flatMap(Collection::stream)
            .collect(toList());
  }

  private List<String> addAdamantEntersTheBattlefieldWithParameters(GameStatus gameStatus, CardInstance cardInstance) {
    var parameters = new ArrayList<String>();
    var adamantAbilities = cardInstance.getAbilitiesByType(ADAMANT);

    for (var adamant : adamantAbilities) {
      var manaPaid = gameStatus.getTurn().getLastManaPaid();
      var manaPaidByColor = manaCountService.countManaPaid(manaPaid);
      var adamantColor = adamant.getParameter(0);
      var adamantFulfilled = isAdamantFulfilled(manaPaidByColor, adamantColor);

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
    for (var parameter : parameters) {
      if (abilityService.tappedFromParameter(parameter)) {
        tapPermanentService.tap(gameStatus, cardInstance.getId());
      }

      var plus1Counters = abilityService.plus1CountersFromParameter(parameter);
      cardInstance.getModifiers().getCounters().addPlus1Counters(plus1Counters);

      var minus1Counters = abilityService.minus1CountersFromParameter(parameter);
      cardInstance.getModifiers().getCounters().addMinus1Counters(minus1Counters);

      var cardsToDraw = abilityService.drawFromParameter(parameter);
      drawXCardsService.drawXCards(gameStatus.getPlayerByName(cardInstance.getController()), cardsToDraw, gameStatus);
    }
  }

}
