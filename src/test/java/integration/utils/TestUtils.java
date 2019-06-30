package integration.utils;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {

  public static GameStatus testGameStatus() {
    GameStatus gameStatus = new GameStatus("game-id");
    gameStatus.setPlayer1(new Player("player-session", "player-name", testLibrary(gameStatus, "player-name")));
    gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", testLibrary(gameStatus, "opponent-name")));
    gameStatus.getTurn().setCurrentTurnPlayer("player-name");
    return gameStatus;
  }

  private static Library testLibrary(GameStatus gameStatus, String playerName) {
    List<CardInstance> libraryCards = IntStream.rangeClosed(1, 60)
            .boxed()
            .map(i -> new CardInstance(gameStatus, i, Cards.PLAINS, playerName))
            .collect(Collectors.toList());

    return new Library(libraryCards);
  }
}