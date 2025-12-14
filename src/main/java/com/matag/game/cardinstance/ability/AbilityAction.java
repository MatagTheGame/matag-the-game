package com.matag.cards.ability;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.status.GameStatus;

public interface AbilityAction {
  void perform(CardInstance cardInstance, GameStatus gameStatus, Ability ability);
}
