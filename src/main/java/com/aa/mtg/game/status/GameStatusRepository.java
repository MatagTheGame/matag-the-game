package com.aa.mtg.game.status;

import com.aa.mtg.security.SecurityHelper;
import com.aa.mtg.security.SecurityToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameStatusRepository {

    private static Map<String, GameStatus> games = new HashMap<>();

    public boolean contains(String gameId) {
        return games.containsKey(gameId);
    }

    public void save(String gameId, GameStatus gameStatus) {
        games.put(gameId, gameStatus);
    }

    public GameStatus getUnsecure(String gameId) {
        return games.get(gameId);
    }

    public GameStatus get(String gameId, SecurityToken token) {
        GameStatus gameStatus = games.get(gameId);
        SecurityHelper.isPlayerAllowedToExecuteAction(gameStatus, token);
        return gameStatus;
    }
}
