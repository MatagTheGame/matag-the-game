package com.aa.mtg.game.status;

import org.springframework.stereotype.Service;

@Service
public class GameStatusRepository {

    private GameStatus gameStatus = new GameStatus();

    public GameStatus get() {
        return gameStatus;
    }

}
