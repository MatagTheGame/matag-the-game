package com.aa.mtg.cardinstance.modifiers;

import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.cards.properties.PowerToughness;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
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
  private List<CardInstanceAbility> abilitiesUntilEndOfTurn = new ArrayList<>();
  @JsonProperty
  private PowerToughness extraPowerToughnessUntilEndOfTurn = new PowerToughness(0, 0);
  @JsonProperty
  private List<Object> targets = new ArrayList<>();
  @JsonProperty
  private int attachedToId;
  private String controller;
  private String controllerUntilEndOfTurn;
  @JsonProperty
  private Counters counters = new Counters();
  private boolean toBeDestroyed;
  private boolean toBeReturnedToHand;

  public void cleanupUntilEndOfTurnModifiers() {
    damage = 0;
    abilitiesUntilEndOfTurn = new ArrayList<>();
    extraPowerToughnessUntilEndOfTurn = new PowerToughness(0, 0);
    controllerUntilEndOfTurn = null;
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

  public PowerToughness getExtraPowerToughnessUntilEndOfTurn() {
    return extraPowerToughnessUntilEndOfTurn;
  }

  public void addExtraPowerToughnessUntilEndOfTurn(PowerToughness extraPowerToughnessUntilEndOfTurn) {
    int newPower = this.extraPowerToughnessUntilEndOfTurn.getPower() + extraPowerToughnessUntilEndOfTurn.getPower();
    int newToughness = this.extraPowerToughnessUntilEndOfTurn.getToughness() + extraPowerToughnessUntilEndOfTurn.getToughness();
    this.extraPowerToughnessUntilEndOfTurn = new PowerToughness(newPower, newToughness);
  }

  public void unsetAttachedId() {
    attachedToId = 0;
  }

  public PowerToughness getExtraPowerToughnessFromCounters() {
    return new PowerToughness(counters.getPlus1Counters(), counters.getPlus1Counters());
  }
}

