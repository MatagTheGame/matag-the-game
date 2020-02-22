package com.aa.mtg.cardinstance.ability;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.ability.CardInstanceAbility;
import com.aa.mtg.game.status.GameStatus;

public interface AbilityAction {
  void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability);
}
