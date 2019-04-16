package com.aa.mtg.utils;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

  public static SimpMessageHeaderAccessor sessionHeader(String sessionId) {
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create();
    headerAccessor.setSessionId(sessionId);
    return headerAccessor;
  }

  public static Library testLibrary(String playerName) {
    List<CardInstance> libraryCards = IntStream.rangeClosed(1, 60)
            .boxed()
            .map(i -> new CardInstance(i, Cards.PLAINS, playerName))
            .collect(Collectors.toList());

    return new Library(libraryCards);
  }

  public static GameStatus testGameStatus() {
    GameStatus gameStatus = new GameStatus("game-id");
    gameStatus.setPlayer1(new Player("player-session", "player-name", testLibrary("player-name")));
    gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", testLibrary("opponent-name")));
    gameStatus.getTurn().setCurrentTurnPlayer("player-name");
    return gameStatus;
  }
}