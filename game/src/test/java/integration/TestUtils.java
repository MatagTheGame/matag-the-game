package integration;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.CardInstanceFactory;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.player.PlayerFactory;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestUtils {
  private final GameStatusFactory gameStatusFactory;
  private final PlayerFactory playerFactory;
  private final CardInstanceFactory cardInstanceFactory;
  private final Cards cards;

  @Autowired
  public TestUtils(GameStatusFactory gameStatusFactory, PlayerFactory playerFactory, CardInstanceFactory cardInstanceFactory, Cards cards) {
    this.gameStatusFactory = gameStatusFactory;
    this.playerFactory = playerFactory;
    this.cardInstanceFactory = cardInstanceFactory;
    this.cards = cards;
  }

  public GameStatus testGameStatus() {
    GameStatus gameStatus = gameStatusFactory.create("game-id");

    gameStatus.setPlayer1(playerFactory.create("player-session", "player-name"));
    gameStatus.getPlayer1().getLibrary().addCards(testLibrary(gameStatus, "player-name"));
    gameStatus.getPlayer1().drawHand();

    gameStatus.setPlayer2(playerFactory.create("opponent-session", "opponent-name"));
    gameStatus.getPlayer2().getLibrary().addCards(testLibrary(gameStatus, "opponent-name"));
    gameStatus.getPlayer2().drawHand();

    gameStatus.getTurn().setCurrentTurnPlayer("player-name");
    gameStatus.getTurn().setCurrentPhaseActivePlayer("player-name");
    gameStatus.getTurn().setCurrentPhase("M1");

    return gameStatus;
  }

  private List<CardInstance> testLibrary(GameStatus gameStatus, String playerName) {
    return IntStream.rangeClosed(1, 40)
            .boxed()
            .map(i -> cardInstanceFactory.create(gameStatus, i, cards.get("Plains"), playerName))
            .collect(Collectors.toList());
  }
}