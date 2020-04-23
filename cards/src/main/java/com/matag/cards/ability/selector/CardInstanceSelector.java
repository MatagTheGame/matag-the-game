package com.matag.cards.ability.selector;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.properties.Color;
import com.matag.cards.properties.Subtype;
import com.matag.cards.properties.Type;
import com.matag.player.PlayerType;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Value
@JsonDeserialize(builder = CardInstanceSelector.CardInstanceSelectorBuilder.class)
@Builder(toBuilder = true)
public class CardInstanceSelector {
  private final SelectorType selectorType;
  private final List<Type> ofType;
  private final List<Type> notOfType;
  private final List<Subtype> ofSubtypeOf;
  private final AbilityType withAbilityType;
  private final List<Color> ofColors;
  private final PowerToughnessConstraint powerToughnessConstraint;
  private final PlayerType controllerType;
  private final List<StatusType> statusTypes;
  private final boolean others;
  private final boolean itself;
  private final boolean nonToken;
  private final boolean currentEnchanted;
  private final TurnStatusType turnStatusType;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CardInstanceSelectorBuilder {

  }

  @JsonIgnore
  public String getText() {
    StringBuilder stringBuilder = new StringBuilder();

    if (selectorType == SelectorType.PERMANENT) {

      if (itself) {
        stringBuilder.append("gets");

      } else {
        if (others) {
          stringBuilder.append("other ");
        }

        if (currentEnchanted) {
          stringBuilder.append("enchanted ");
        }

        if (ofType != null) {
          stringBuilder.append(ofType.stream().map(Objects::toString).collect(Collectors.joining(", "))).append("s ");
        }

        if (controllerType != null) {
          if (controllerType == PlayerType.PLAYER) {
            stringBuilder.append("you control ");
          } else {
            stringBuilder.append("opponent controls ");
          }
        }

        stringBuilder.append("get");
      }
    }

    String str = stringBuilder.toString();
    str = str.toLowerCase();
    str = str.isEmpty() ? str : str.substring(0, 1).toUpperCase() + str.substring(1);

    return str.trim();
  }
}
