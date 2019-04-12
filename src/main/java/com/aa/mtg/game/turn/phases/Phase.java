package com.aa.mtg.game.turn.phases;

import com.aa.mtg.game.status.GameStatus;

public interface Phase {
    
    void apply(GameStatus gameStatus);
    
}
