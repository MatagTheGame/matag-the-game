package com.matag.game.turn.action.selection;

import com.matag.cards.ability.AbilityService;
import com.matag.cards.ability.trigger.TriggerType;
import com.matag.cards.properties.PowerToughness;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.matag.cards.ability.type.AbilityType.SELECTED_PERMANENTS_GET;

@Component
@AllArgsConstructor
public class AbilitiesFromOtherPermanentsService {
  private final MagicInstancePermanentSelectorService magicInstancePermanentSelectorService;
  private final AbilityService abilityService;
  private final CardInstanceAbilityFactory cardInstanceAbilityFactory;

  public int getPowerFromOtherPermanents(GameStatus gameStatus, CardInstance cardInstance) {
    return getParametersFromOtherPermanents(gameStatus, cardInstance).stream()
      .map(abilityService::powerToughnessFromParameter)
      .map(PowerToughness::getPower)
      .reduce(Integer::sum)
      .orElse(0);
  }

  public int getToughnessFromOtherPermanents(GameStatus gameStatus, CardInstance cardInstance) {
    return getParametersFromOtherPermanents(gameStatus, cardInstance).stream()
      .map(abilityService::powerToughnessFromParameter)
      .map(PowerToughness::getToughness)
      .reduce(Integer::sum)
      .orElse(0);
  }

  public List<CardInstanceAbility> getAbilitiesFormOtherPermanents(GameStatus gameStatus, CardInstance cardInstance) {
    return cardInstanceAbilityFactory.abilitiesFromParameters(getParametersFromOtherPermanents(gameStatus, cardInstance));
  }

  private List<String> getParametersFromOtherPermanents(GameStatus gameStatus, CardInstance cardInstance) {
    var parameters = new ArrayList<String>();
    var cards = gameStatus.getAllBattlefieldCardsSearch().withFixedAbility(SELECTED_PERMANENTS_GET).getCards();

    for (var card : cards) {
      for (var ability : card.getFixedAbilitiesByType(SELECTED_PERMANENTS_GET)) {
        if (ability.getTrigger().getType() == TriggerType.STATIC) {
          if (magicInstancePermanentSelectorService.select(gameStatus, card, ability.getMagicInstanceSelector()).withId(cardInstance.getId()).isPresent()) {
            parameters.addAll(ability.getParameters());
          }
        }
      }
    }

    return parameters;
  }
}
