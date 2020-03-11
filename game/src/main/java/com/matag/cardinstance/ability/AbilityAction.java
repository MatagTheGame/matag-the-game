package com.matag.cardinstance.ability;

import com.matag.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;

public interface AbilityAction {
  void perform(CardInstance cardInstance, GameStatus gameStatus, CardInstanceAbility ability);
}
