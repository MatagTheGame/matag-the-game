package integration;

import com.matag.adminentities.PlayerInfo;
import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.player.PlayerFactory;
import com.matag.game.security.SecurityToken;
import com.matag.game.status.GameStatus;
import com.matag.game.status.GameStatusFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.matag.game.turn.phases.main1.Main1Phase.M1;

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

    PlayerInfo player1 = new PlayerInfo("player-name");
    SecurityToken player1SecurityToken = new SecurityToken("player-session", UUID.randomUUID().toString(), "1");
    gameStatus.setPlayer1(playerFactory.create(player1SecurityToken, player1));
    gameStatus.getPlayer1().getLibrary().addCards(testLibrary(gameStatus, player1.getPlayerName()));
    gameStatus.getPlayer1().drawHand();

    PlayerInfo player2 = new PlayerInfo("opponent-name");
    SecurityToken player2SecurityToken = new SecurityToken("opponent-session", UUID.randomUUID().toString(), "1");
    gameStatus.setPlayer2(playerFactory.create(player2SecurityToken, player2));
    gameStatus.getPlayer2().getLibrary().addCards(testLibrary(gameStatus, player2.getPlayerName()));
    gameStatus.getPlayer2().drawHand();

    gameStatus.getTurn().setCurrentTurnPlayer("player-name");
    gameStatus.getTurn().setCurrentPhaseActivePlayer("player-name");
    gameStatus.getTurn().setCurrentPhase(M1);

    return gameStatus;
  }

  private List<CardInstance> testLibrary(GameStatus gameStatus, String playerName) {
    return IntStream.rangeClosed(1, 40)
      .boxed()
      .map(i -> cardInstanceFactory.create(gameStatus, i, cards.get("Plains"), playerName))
      .collect(Collectors.toList());
  }
}