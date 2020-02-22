package com.aa.mtg.cards.ability.selector;

import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Subtype;
import com.aa.mtg.cards.properties.Type;
import com.aa.mtg.player.PlayerType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@Builder
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
  private final TurnStatusType turnStatusType;

  @JsonCreator
  private CardInstanceSelector(@JsonProperty("selectorType") SelectorType selectorType, @JsonProperty("ofType") List<Type> ofType,
                               @JsonProperty("notOfType") List<Type> notOfType, @JsonProperty("ofSubtypeOf") List<Subtype> ofSubtypeOf,
                               @JsonProperty("withAbilityType") AbilityType withAbilityType, @JsonProperty("ofColors") List<Color> ofColors,
                               @JsonProperty("powerToughnessConstraint") PowerToughnessConstraint powerToughnessConstraint, @JsonProperty("controllerType") PlayerType controllerType,
                               @JsonProperty("statusTypes") List<StatusType> statusTypes, @JsonProperty("others") boolean others,
                               @JsonProperty("itself") boolean itself, @JsonProperty("nonToken") boolean nonToken,
                               @JsonProperty("turnStatusType") TurnStatusType turnStatusType) {
    this.selectorType = selectorType;
    this.ofType = ofType;
    this.notOfType = notOfType;
    this.ofSubtypeOf = ofSubtypeOf;
    this.withAbilityType = withAbilityType;
    this.ofColors = ofColors;
    this.powerToughnessConstraint = powerToughnessConstraint;
    this.controllerType = controllerType;
    this.statusTypes = statusTypes;
    this.others = others;
    this.itself = itself;
    this.nonToken = nonToken;
    this.turnStatusType = turnStatusType;
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
