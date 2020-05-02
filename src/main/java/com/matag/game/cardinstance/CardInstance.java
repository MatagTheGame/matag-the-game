package com.matag.game.cardinstance;

import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.cardinstance.modifiers.CardModifiers;
import com.matag.cards.Card;
import com.matag.cards.CardUtils;
import com.matag.cards.ability.trigger.TriggerSubtype;
import com.matag.cards.ability.trigger.TriggerType;
import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.properties.Color;
import com.matag.cards.properties.Subtype;
import com.matag.cards.properties.Type;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.attach.AttachmentsService;
import com.matag.game.turn.action.selection.AbilitiesFromOtherPermanentsService;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.matag.game.cardinstance.ability.CardInstanceAbility.getCardInstanceAbilities;
import static com.matag.cards.properties.Type.INSTANT;
import static com.matag.cards.properties.Type.SORCERY;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

@Data
@JsonAutoDetect(
  fieldVisibility = JsonAutoDetect.Visibility.NONE,
  setterVisibility = JsonAutoDetect.Visibility.NONE,
  getterVisibility = JsonAutoDetect.Visibility.NONE,
  isGetterVisibility = JsonAutoDetect.Visibility.NONE,
  creatorVisibility = JsonAutoDetect.Visibility.NONE
)
@Component
@Scope("prototype")
public class CardInstance {

  private int id;
  @JsonProperty
  private Card card;
  @JsonProperty
  private String owner;
  private String controller;
  @JsonProperty
  private CardModifiers modifiers = new CardModifiers();
  @JsonProperty
  private List<CardInstanceAbility> triggeredAbilities = new ArrayList<>();
  private Set<String> acknowledgedBy = new HashSet<>();

  private GameStatus gameStatus;
  private final AttachmentsService attachmentsService;
  private final AbilitiesFromOtherPermanentsService abilitiesFromOtherPermanentsService;

  public CardInstance(
    @Autowired(required = false) AttachmentsService attachmentsService,
    @Autowired(required = false) AbilitiesFromOtherPermanentsService abilitiesFromOtherPermanentsService
  ) {
    this.attachmentsService = attachmentsService;
    this.abilitiesFromOtherPermanentsService = abilitiesFromOtherPermanentsService;
  }

  @JsonProperty
  public int getId() {
    return modifiers.getPermanentId() > 0 ? modifiers.getPermanentId() : id;
  }

  @JsonProperty
  public String getController() {
    if (modifiers.getModifiersUntilEndOfTurn().getNewController() != null) {
      return modifiers.getModifiersUntilEndOfTurn().getNewController();
    }

    if (modifiers.getController() != null) {
      return modifiers.getController();
    }

    return controller;
  }

  @JsonProperty
  public int getPower() {
    return card.getPower() +
      modifiers.getModifiersUntilEndOfTurn().getExtraPowerToughness().getPower() +
      modifiers.getExtraPowerToughnessFromCounters().getPower() +
      getAttachmentsPower() +
      getPowerFromOtherPermanents();
  }

  @JsonProperty
  public int getToughness() {
    return card.getToughness() +
      modifiers.getModifiersUntilEndOfTurn().getExtraPowerToughness().getToughness() +
      modifiers.getExtraPowerToughnessFromCounters().getToughness() +
      getAttachmentsToughness() +
      getToughnessFromOtherPermanents();
  }

  @JsonProperty
  public List<CardInstanceAbility> getAbilities() {
    List<CardInstanceAbility> abilities = getFixedAbilities();
    abilities.addAll(getAbilitiesFormOtherPermanents());
    return abilities;
  }

  @JsonProperty
  public boolean isSummoningSickness() {
    return modifiers.isSummoningSickness() && !hasAbilityType(AbilityType.HASTE);
  }

  @JsonProperty
  public boolean isInstantSpeed() {
    return card.getTypes().contains(INSTANT) || hasAbilityType(AbilityType.FLASH);
  }

  public String getIdAndName() {
    return "\"" + getId() + " - " + card.getName() + "\"";
  }

  public boolean isOfType(Type type) {
    return CardUtils.isOfType(card, type);
  }

  public boolean ofAnyOfTheTypes(List<Type> types) {
    for (Type type : types) {
      if (isOfType(type)) {
        return true;
      }
    }
    return false;
  }

  public boolean ofAnyOfTheSubtypes(List<Subtype> subtypes) {
    for (Subtype subtype : subtypes) {
      if (isOfSubtype(subtype)) {
        return true;
      }
    }
    return false;
  }

  public boolean isOfColor(Color color) {
    return CardUtils.isOfColor(card, color);
  }

  public boolean ofAnyOfTheColors(List<Color> colors) {
    for (Color color : colors) {
      if (isOfColor(color)) {
        return true;
      }
    }
    return false;
  }

  public void checkIfCanAttack() {
    if (!isOfType(Type.CREATURE)) {
      throw new MessageException(getIdAndName() + " is not of type Creature.");
    }

    if (modifiers.isTapped()) {
      throw new MessageException(getIdAndName() + " is already tapped and cannot attack.");
    }

    if (isSummoningSickness()) {
      throw new MessageException(getIdAndName() + " has summoning sickness and cannot attack.");
    }
  }

  public void declareAsAttacker() {
    if (!hasAbilityType(AbilityType.VIGILANCE)) {
      modifiers.tap();
    }
    modifiers.setAttacking(true);
  }

  public void checkIfCanBlock(CardInstance blockedCreature, List<CardInstance> blockers) {
    if (!isOfType(Type.CREATURE)) {
      throw new MessageException(getIdAndName() + " is not of type Creature.");
    }

    if (modifiers.isTapped()) {
      throw new MessageException(getIdAndName() + " is tapped and cannot block.");
    }

    if (blockedCreature.hasAbilityType(AbilityType.FLYING)) {
      if (!(hasAbilityType(AbilityType.FLYING) || hasAbilityType(AbilityType.REACH))) {
        throw new MessageException(getIdAndName() + " cannot block " + blockedCreature.getIdAndName() + " as it has flying.");
      }
    }

    if (blockedCreature.hasAbilityType(AbilityType.MENACE) && blockers.size() < 2) {
      throw new MessageException(getIdAndName() + " cannot block " + blockedCreature.getIdAndName() + " as it has menace.");
    }
  }

  public void declareAsBlocker(int attackingCreatureId) {
    modifiers.setBlockingCardId(attackingCreatureId);
  }

  public void acknowledgedBy(String playerName) {
    acknowledgedBy.add(playerName);
  }

  public List<CardInstanceAbility> getFixedAbilities() {
    List<CardInstanceAbility> abilities = new ArrayList<>();
    abilities.addAll(getCardInstanceAbilities(card));
    abilities.addAll(modifiers.getAbilities());
    abilities.addAll(modifiers.getModifiersUntilEndOfTurn().getExtraAbilities());
    abilities.addAll(getAttachmentsAbilities());
    return abilities;
  }

  public boolean canProduceMana(Color color) {
    return getAbilitiesByTriggerType(TriggerType.MANA_ABILITY).stream()
      .flatMap(ability -> ability.getParameters().stream())
      .anyMatch(parameter -> parameter.equals(color.toString()));
  }

  public List<CardInstanceAbility> getAbilitiesByTriggerType(TriggerType triggerType) {
    return getAbilities().stream()
      .filter(ability -> ability.getTrigger() != null)
      .filter(ability -> ability.getTrigger().getType().equals(triggerType))
      .collect(toList());
  }

  public List<CardInstanceAbility> getAbilitiesByTriggerSubType(TriggerSubtype triggerSubType) {
    return getAbilities().stream()
      .filter(ability -> ability.getTrigger() != null)
      .filter(ability -> triggerSubType.equals(ability.getTrigger().getSubtype()))
      .collect(toList());
  }

  public List<CardInstanceAbility> getAbilitiesByType(AbilityType abilityType) {
    return getAbilities().stream()
      .filter(currentAbility -> currentAbility.getAbilityType().equals(abilityType))
      .collect(toList());
  }

  public boolean hasAbilityType(AbilityType abilityType) {
    return getAbilitiesByType(abilityType).size() > 0;
  }

  public List<CardInstanceAbility> getFixedAbilitiesByType(AbilityType abilityType) {
    return getFixedAbilities().stream()
      .filter(currentAbility -> currentAbility.getAbilityType().equals(abilityType))
      .collect(toList());
  }

  public boolean hasFixedAbility(AbilityType abilityType) {
    return getFixedAbilitiesByType(abilityType).size() > 0;
  }

  public boolean hasAnyFixedAbility(List<AbilityType> abilityTypes) {
    return abilityTypes.stream().anyMatch( abilityType -> getFixedAbilitiesByType(abilityType).size() > 0);
  }

  public boolean hasFixedAbilityWithTriggerSubType(TriggerSubtype triggerSubtype) {
    return getAbilitiesByTriggerSubType(triggerSubtype).size() > 0;
  }

  public boolean isPermanent() {
    return !(isOfType(INSTANT) || isOfType(SORCERY));
  }

  public boolean isOfSubtype(Subtype subtype) {
    return this.card.getSubtypes().contains(subtype);
  }

  public void cleanup() {
    modifiers.cleanupUntilEndOfTurnModifiers();
    acknowledgedBy.clear();
  }

  public void resetAllModifiers() {
    modifiers = new CardModifiers();
  }

  private int getAttachmentsPower() {
    return attachmentsService != null ? attachmentsService.getAttachmentsPower(gameStatus, this) : 0;
  }

  private int getAttachmentsToughness() {
    return attachmentsService != null ? attachmentsService.getAttachmentsToughness(gameStatus, this) : 0;
  }

  private List<CardInstanceAbility> getAttachmentsAbilities() {
    return attachmentsService != null ? attachmentsService.getAttachmentsAbilities(gameStatus, this) : emptyList();
  }

  private int getPowerFromOtherPermanents() {
    return abilitiesFromOtherPermanentsService != null ? abilitiesFromOtherPermanentsService.getPowerFromOtherPermanents(gameStatus, this) : 0;
  }

  private int getToughnessFromOtherPermanents() {
    return abilitiesFromOtherPermanentsService != null ? abilitiesFromOtherPermanentsService.getToughnessFromOtherPermanents(gameStatus, this) : 0;
  }

  private List<CardInstanceAbility> getAbilitiesFormOtherPermanents() {
    return abilitiesFromOtherPermanentsService != null ? abilitiesFromOtherPermanentsService.getAbilitiesFormOtherPermanents(gameStatus, this) : emptyList();
  }
}
