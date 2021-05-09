package com.matag.game.cardinstance;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.matag.cards.ability.selector.PowerToughnessConstraint;
import com.matag.cards.ability.selector.TurnStatusType;
import com.matag.cards.ability.trigger.TriggerSubtype;
import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.properties.Color;
import com.matag.cards.properties.Subtype;
import com.matag.cards.properties.Type;
import com.matag.game.cardinstance.ability.selector.PowerToughnessConstraintUtils;
import com.matag.game.status.GameStatus;

public class CardInstanceSearch {

  private final Stream<CardInstance> cards;

  public CardInstanceSearch(List<CardInstance> cards) {
    this.cards = cards.stream();
  }

  public CardInstanceSearch(Stream<CardInstance> cards) {
    this.cards = cards;
  }

  public Optional<CardInstance> withId(int cardId) {
    return cards
      .filter(cardInstance -> cardInstance.getId() == cardId)
      .findFirst();
  }

  public CardInstanceSearch withIdAsList(int cardId) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getId() == cardId));
  }

  public CardInstanceSearch notWithId(int cardId) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getId() != cardId));
  }

  public CardInstanceSearch withName(String name) {
    return new CardInstanceSearch(this.cards.filter((ci -> ci.getCard().getName().equals(name))));
  }

  public CardInstanceSearch ofType(Type type) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.isOfType(type)));
  }

  public CardInstanceSearch ofAnyOfTheTypes(List<Type> types) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.ofAnyOfTheTypes(types)));
  }

  public CardInstanceSearch notOfTypes(List<Type> types) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> !cardInstance.ofAnyOfTheTypes(types)));
  }

  public CardInstanceSearch ofAllOfTheTypes(List<Type> types) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.ofAllOfTheTypes(types)));
  }

  public CardInstanceSearch ofAnyOfTheSubtypes(List<Subtype> subtypes) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.ofAnyOfTheSubtypes(subtypes)));
  }

  public CardInstanceSearch notOfSubtypes(List<Subtype> subtypes) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> !cardInstance.ofAnyOfTheSubtypes(subtypes)));
  }

  public CardInstanceSearch ofAnyOfTheColors(List<Color> colors) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.ofAnyOfTheColors(colors)));
  }

  public CardInstanceSearch colorless() {
    return new CardInstanceSearch(this.cards.filter(CardInstance::isColorless));
  }

  public CardInstanceSearch multicolor() {
    return new CardInstanceSearch(this.cards.filter(CardInstance::isMulticolor));
  }

  public CardInstanceSearch tapped() {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getModifiers().isTapped()));
  }

  public CardInstanceSearch untapped() {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getModifiers().isUntapped()));
  }

  public CardInstanceSearch withSummoningSickness() {
    return new CardInstanceSearch(this.cards
      .filter(CardInstance::isSummoningSickness));
  }

  public CardInstanceSearch withoutSummoningSickness() {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> !cardInstance.isSummoningSickness()));
  }

  public CardInstanceSearch attacking() {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getModifiers().isAttacking()));
  }

  public CardInstanceSearch blocking() {
    return new CardInstanceSearch(this.cards
        .filter(cardInstance -> cardInstance.getModifiers().isBlocking()));
  }

  public CardInstanceSearch attackingOrBlocking() {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getModifiers().isAttacking() || cardInstance.getModifiers().isBlocking()));
  }

  public CardInstanceSearch attacked() {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getModifiers().getModifiersUntilEndOfTurn().isAttacked()));
  }

  public CardInstanceSearch blocked() {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getModifiers().getModifiersUntilEndOfTurn().isBlocked()));
  }

  public CardInstanceSearch attackedOrBlocked() {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getModifiers().getModifiersUntilEndOfTurn().isAttacked() ||
        cardInstance.getModifiers().getModifiersUntilEndOfTurn().isBlocked()));
  }

  public CardInstanceSearch ofPowerToughnessConstraint(PowerToughnessConstraint powerToughnessConstraint) {
    return new CardInstanceSearch(this.cards
      .filter(card -> PowerToughnessConstraintUtils.check(powerToughnessConstraint, card)));
  }

  public CardInstanceSearch controlledBy(String playerName) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.getController().equals(playerName)));
  }

  public CardInstanceSearch notControlledBy(String playerName) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> !cardInstance.getController().equals(playerName)));
  }

  public CardInstanceSearch withFixedAbility(AbilityType abilityType) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.hasFixedAbility(abilityType)));
  }

  public CardInstanceSearch withAnyFixedAbility(List<AbilityType> abilityTypes) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.hasAnyFixedAbility(abilityTypes)));
  }

  public CardInstanceSearch withoutFixedAbility(AbilityType abilityType) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> !cardInstance.hasFixedAbility(abilityType)));
  }

  public CardInstanceSearch withTriggerSubtype(TriggerSubtype triggerSubtype) {
    return new CardInstanceSearch(this.cards
      .filter(cardInstance -> cardInstance.hasFixedAbilityWithTriggerSubType(triggerSubtype)));
  }

  public CardInstanceSearch onTurnStatusType(TurnStatusType turnStatusType, GameStatus gameStatus) {
    if (turnStatusType == TurnStatusType.YOUR_TURN) {
      return controlledBy(gameStatus.getCurrentPlayer().getName());
    } else if (turnStatusType == TurnStatusType.OPPONENT_TURN) {
      return controlledBy(gameStatus.getNonCurrentPlayer().getName());
    }
    return this;
  }

  public CardInstanceSearch attachedToId(int attachedToId) {
    return new CardInstanceSearch(this.cards
      .filter((cardInstance -> cardInstance.getModifiers().getAttachedToId() == attachedToId)));
  }

  public CardInstanceSearch notAcknowledged() {
    return new CardInstanceSearch(this.cards.filter((CardInstance::notAcknowledged)));
  }

  public boolean canAnyCreatureAttack() {
    return ofType(Type.CREATURE)
      .untapped()
      .withoutSummoningSickness()
      .isNotEmpty();
  }

  public boolean canAnyCreatureBlock() {
    return ofType(Type.CREATURE)
      .untapped()
      .isNotEmpty();
  }
  
  public CardInstanceSearch withInstantSpeed() {
    return new CardInstanceSearch(this.cards.filter((CardInstance::isInstantSpeed)));
  }

  public CardInstanceSearch historic() {
    return new CardInstanceSearch(this.cards.filter((CardInstance::isHistoric)));
  }

  public CardInstanceSearch concat(List<CardInstance> moreCards) {
    return new CardInstanceSearch(Stream.concat(this.cards, moreCards.stream()));
  }

  public CardInstanceSearch concat(CardInstanceSearch moreCards) {
    return concat(moreCards.getCards());
  }

  public boolean isEmpty() {
    return cards.count() == 0;
  }

  public boolean isNotEmpty() {
    return cards.count() > 0;
  }

  public List<CardInstance> getCards() {
    return cards.collect(Collectors.toList());
  }
}
