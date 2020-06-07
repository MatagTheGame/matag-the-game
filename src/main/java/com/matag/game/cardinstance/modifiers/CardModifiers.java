package com.matag.game.cardinstance.modifiers;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.matag.cards.properties.PowerToughness;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@JsonAutoDetect(
  fieldVisibility = JsonAutoDetect.Visibility.NONE,
  setterVisibility = JsonAutoDetect.Visibility.NONE,
  getterVisibility = JsonAutoDetect.Visibility.NONE,
  isGetterVisibility = JsonAutoDetect.Visibility.NONE,
  creatorVisibility = JsonAutoDetect.Visibility.NONE
)
public class CardModifiers {
  @JsonProperty
  private int permanentId;
  @JsonProperty
  private TappedModifier tapped;
  @JsonProperty
  private boolean doesNotUntapNextTurn;
  private boolean summoningSickness;
  @JsonProperty
  private boolean attacking;
  @JsonProperty
  private Integer blockingCardId;
  @JsonProperty
  private int damage;
  @JsonProperty
  private List<CardInstanceAbility> abilities = new ArrayList<>();
  @JsonProperty
  private List<Object> targets = new ArrayList<>();
  @JsonProperty
  private int attachedToId;
  private String controller;
  @JsonProperty
  private Counters counters = new Counters();
  private CardModifiersUntilEndOfTurn modifiersUntilEndOfTurn = new CardModifiersUntilEndOfTurn();

  public void cleanupUntilEndOfTurnModifiers() {
    damage = 0;
    modifiersUntilEndOfTurn = new CardModifiersUntilEndOfTurn();
  }

  public boolean isTapped() {
    return tapped != null;
  }

  public boolean isUntapped() {
    return !isTapped();
  }

  public void tap() {
    this.tapped = TappedModifier.TAPPED;
  }

  public void untap() {
    this.tapped = null;
  }

  public void doesNotUntapNextTurn() {
    this.doesNotUntapNextTurn = true;
  }

  public Integer getBlockingCardId() {
    return blockingCardId;
  }

  public boolean isBlocking() {
    return blockingCardId != null;
  }

  public void unsetBlockingCardId() {
    this.blockingCardId = null;
  }

  public void dealDamage(int damage) {
    this.damage += damage;
  }

  public void unsetAttachedId() {
    attachedToId = 0;
  }

  public PowerToughness getExtraPowerToughnessFromCounters() {
    return new PowerToughness(counters.getPlus1Counters(), counters.getPlus1Counters());
  }

  public void resetTargets() {
    targets = new ArrayList<>();
  }

  public void addTargets(List<Object> targetsIds) {
    targets.addAll(targetsIds);
  }
}

