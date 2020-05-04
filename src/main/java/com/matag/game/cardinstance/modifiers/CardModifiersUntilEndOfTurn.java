package com.matag.game.cardinstance.modifiers;

import com.matag.cards.properties.PowerToughness;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CardModifiersUntilEndOfTurn {
  private boolean attacked;
  private boolean blocked;
  private List<CardInstanceAbility> extraAbilities = new ArrayList<>();
  private PowerToughness extraPowerToughness = new PowerToughness(0, 0);
  private String newController;
  private boolean toBeDestroyed;
  private boolean toBeReturnedToHand;

  public void addExtraPowerToughnessUntilEndOfTurn(PowerToughness extraPowerToughness) {
    int newPower = this.extraPowerToughness.getPower() + extraPowerToughness.getPower();
    int newToughness = this.extraPowerToughness.getToughness() + extraPowerToughness.getToughness();
    this.extraPowerToughness = new PowerToughness(newPower, newToughness);
  }
}
