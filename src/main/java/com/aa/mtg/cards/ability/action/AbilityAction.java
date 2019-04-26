package com.aa.mtg.cards.ability.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.status.GameStatus;

import java.util.List;

public interface AbilityAction {
    void perform(CardInstance cardInstance, GameStatus gameStatus, List<Integer> targetCardIds);
}
