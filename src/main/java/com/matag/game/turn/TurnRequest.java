package com.matag.game.turn;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class TurnRequest {
  String action;
  String inputRequiredAction;
  String inputRequiredActionParameter;
  String inputRequiredChoices;
  Map<Integer, List<String>> mana;
  List<Integer> cardIds;
  Map<Integer, List<Object>> targetsIdsForCardIds;
  String playedAbility;
}
