package com.matag.game.turn.action.selection;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory;
import com.matag.cards.ability.AbilityService;
import com.matag.cards.ability.trigger.TriggerType;
import com.matag.cards.properties.PowerToughness;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.matag.cards.ability.type.AbilityType.SELECTED_PERMANENTS_GET;

@Component
@AllArgsConstructor
public class AbilitiesFromOtherPermanentsService {
  private final CardInstanceSelectorService cardInstanceSelectorService;
  private final AbilityService abilityService;
  private final CardInstanceAbilityFactory cardInstanceAbilityFactory;

  public void updateGameStatus(GameStatus gameStatus) {
    Stream.of(gameStatus.getPlayer1(), gameStatus.getPlayer2())
            .map(Player::getBattlefield)
            .flatMap(battlefield -> battlefield.getCards().stream())
            .forEach(cardInstance -> {
              cardInstance.setPowerFromOtherPermanents(getPowerFromOtherPermanents(gameStatus, cardInstance));
              cardInstance.setToughnessFromOtherPermanents(getToughnessFromOtherPermanents(gameStatus, cardInstance));
              cardInstance.setAbilitiesFormOtherPermanents(getAbilitiesFormOtherPermanents(gameStatus, cardInstance));
            });
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
