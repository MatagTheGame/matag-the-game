package com.matag.game.turn.action.attach;

import static com.matag.cards.ability.type.AbilityType.ENCHANT;
import static com.matag.cards.ability.type.AbilityType.EQUIP;
import static java.util.Arrays.asList;

import java.util.List;
import java.util.stream.Collectors;

import com.matag.cards.ability.Ability;
import org.springframework.stereotype.Component;

import com.matag.cards.ability.AbilityService;
import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.properties.PowerToughness;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.ability.CardInstanceAbilityFactory;
import com.matag.game.status.GameStatus;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class AttachmentsService {
  private static final List<AbilityType> ATTACHED_ABILITY_TYPES = asList(ENCHANT, EQUIP);

  private final AbilityService abilityService;
  private final CardInstanceAbilityFactory cardInstanceAbilityFactory;

  public List<CardInstance> getAttachedCards(GameStatus gameStatus, CardInstance cardInstance) {
    return gameStatus.getAllBattlefieldCardsSearch().attachedToId(cardInstance.getId()).getCards();
  }

  public int getAttachmentsPower(GameStatus gameStatus, CardInstance cardInstance) {
    return getAttachedCardsAbilities(gameStatus, cardInstance).stream()
      .map(ability -> abilityService.powerToughnessFromParameters(ability.getAbility().getParameters()))
      .map(PowerToughness::getPower)
      .reduce(Integer::sum)
      .orElse(0);
  }

  public int getAttachmentsToughness(GameStatus gameStatus, CardInstance cardInstance) {
    return getAttachedCardsAbilities(gameStatus, cardInstance).stream()
      .map(ability -> abilityService.powerToughnessFromParameters(ability.getAbility().getParameters()))
      .map(PowerToughness::getToughness)
      .reduce(Integer::sum)
      .orElse(0);
  }

  public List<CardInstanceAbility> getAttachmentsAbilities(GameStatus gameStatus, CardInstance cardInstance) {
    return getAttachedCardsAbilities(gameStatus, cardInstance).stream()
      .flatMap(ability -> cardInstanceAbilityFactory.abilitiesFromParameters(ability.getAbility().getParameters()).stream())
      .collect(Collectors.toList());
  }

  private List<CardInstanceAbility> getAttachedCardsAbilities(GameStatus gameStatus, CardInstance cardInstance) {
    return getAttachedCards(gameStatus, cardInstance).stream()
      .flatMap(attachedCard -> attachedCard.getCard().getAbilities().stream())
      .map(CardInstanceAbility::new)
      .filter(ability -> ATTACHED_ABILITY_TYPES.contains(ability.getAbility().getAbilityType()))
      .collect(Collectors.toList());
  }
}
