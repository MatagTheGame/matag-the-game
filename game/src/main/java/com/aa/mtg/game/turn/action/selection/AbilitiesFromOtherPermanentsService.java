package com.aa.mtg.game.turn.action.selection;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cardinstance.ability.CardInstanceAbilityFactory;
import com.aa.mtg.cards.ability.AbilityService;
import com.aa.mtg.cards.ability.trigger.TriggerType;
import com.aa.mtg.cards.properties.PowerToughness;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.ability.type.AbilityType.SELECTED_PERMANENTS_GET;

@Component
public class AbilitiesFromOtherPermanentsService {

  private final CardInstanceSelectorService cardInstanceSelectorService;
  private final AbilityService abilityService;
  private final CardInstanceAbilityFactory cardInstanceAbilityFactory;

  public AbilitiesFromOtherPermanentsService(CardInstanceSelectorService cardInstanceSelectorService, AbilityService abilityService, CardInstanceAbilityFactory cardInstanceAbilityFactory) {
    this.cardInstanceSelectorService = cardInstanceSelectorService;
    this.abilityService = abilityService;
    this.cardInstanceAbilityFactory = cardInstanceAbilityFactory;
  }

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
    List<String> parameters = new ArrayList<>();
    List<CardInstance> cards = gameStatus.getAllBattlefieldCards().withFixedAbility(SELECTED_PERMANENTS_GET).getCards();

    for (CardInstance card : cards) {
      for (CardInstanceAbility ability : card.getFixedAbilitiesByType(SELECTED_PERMANENTS_GET)) {
        if (ability.getTrigger().getType() == TriggerType.STATIC) {
          if (cardInstanceSelectorService.select(gameStatus, card, ability.getCardInstanceSelector()).withId(cardInstance.getId()).isPresent()) {
            parameters.addAll(ability.getParameters());
          }
        }
      }
    }

    return parameters;
  }
}
