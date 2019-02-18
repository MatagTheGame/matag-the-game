package com.aa.mtg.game.status;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GameStatusRepository {

    private static Map<String, GameStatus> games;
    private GameStatus gameStatus = new GameStatus();

    public GameStatus get() {
        return gameStatus;
    }

}
