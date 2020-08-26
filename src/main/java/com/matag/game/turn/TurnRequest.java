package com.matag.game.turn;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import java.util.List;
import java.util.Map;

@Value
@JsonDeserialize(builder = TurnRequest.TurnRequestBuilder.class)
@Builder
public class TurnRequest {
  String action;
  String inputRequiredAction;
  String inputRequiredActionParameter;
  Map<Integer, List<String>> mana;
  List<Integer> cardIds;
  Map<Integer, List<Object>> targetsIdsForCardIds;
  String playedAbility;

  @JsonPOJOBuilder(withPrefix = "")
  public static class TurnRequestBuilder {}

}
