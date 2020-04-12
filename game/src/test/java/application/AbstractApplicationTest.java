package application;

import application.browser.MatagBrowser;
import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.launcher.LauncherGameResponseBuilder;
import com.matag.game.launcher.LauncherTestGameController;
import com.matag.game.player.playerInfo.PlayerInfo;
import com.matag.game.player.playerInfo.PlayerInfoRetriever;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.atomic.AtomicInteger;

import static com.matag.game.turn.phases.Main1Phase.M1;
import static com.matag.game.turn.phases.UpkeepPhase.UP;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static java.lang.Integer.parseInt;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

public abstract class AbstractApplicationTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationTest.class);

  private static AtomicInteger GAME_ID = new AtomicInteger(0);

  @LocalServerPort
  private int port;

  protected MatagBrowser browser;

  @Autowired
  private PlayerInfoRetriever playerInfoRetriever;

  public abstract void setupGame();

  @Before
  public void setupWithRetries() {
    given(playerInfoRetriever.retrieve(any())).willReturn(
      new PlayerInfo("Player1"),
      new PlayerInfo("Player2")
    );

    RuntimeException lastException = null;

    for (int i = 0; i < getNumOfRetries(); i++) {
      try {
        lastException = null;
        LOGGER.info("Setup (attempt {})", i);
        setup();
        LOGGER.info("Setup succeeded");
        break;

      } catch (RuntimeException e) {
        sleep5Secs();
        lastException = e;
        LOGGER.info("Setup failed: {}", e.getMessage());
      }
    }

    if (lastException != null) {
      throw lastException;
    }
  }

  private void setup() {
    setupGame();

    // When player1 joins the game is waiting for opponent
    browser = new MatagBrowser(port, GAME_ID.incrementAndGet());
    browser.getMessageHelper().hasMessage("Waiting for opponent...");

    // When player2 joins the game both players see the table with the cards
    browser.openSecondTab();

    // Make sure player1 is Player1 and player2 is Player2
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveName();
    browser.player2().getPlayerInfoHelper(PLAYER).toHaveName();
    if (browser.player1().getPlayerInfoHelper(PLAYER).getPlayerName().equals("Player2")) {
      browser.swapTabs();
    }
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveName("Player1");
    browser.player2().getPlayerInfoHelper(PLAYER).toHaveName("Player2");

    // Message disappears
    browser.player1().getMessageHelper().hasNoMessage();
    browser.player2().getMessageHelper().hasNoMessage();

    // Status and Phase are
    browser.player1().getPhaseHelper().is(UP, PLAYER);
    browser.player1().getStatusHelper().hasMessage("Play any instant or ability or continue (SPACE).");
    browser.player2().getPhaseHelper().is(UP, OPPONENT);
    browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");

    // Player1 continues
    browser.player1().getActionHelper().clickContinue();

    // Status and Phase are
    browser.player1().getPhaseHelper().is(UP, OPPONENT);
    browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    browser.player2().getPhaseHelper().is(UP, PLAYER);
    browser.player2().getStatusHelper().hasMessage("Play any instant or ability or continue (SPACE).");

    // Player2 continues
    browser.player2().getActionHelper().clickContinue();

    // Status is
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
    browser.player2().getPhaseHelper().is(M1, OPPONENT);
    browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
  }

  @After
  public void cleanup() {
    //browser.dumpContent();
    browser.close();
  }

  @Configuration
  public static class InitGameTestConfiguration {
    @Bean
    public InitTestServiceDecorator initTestServiceDecorator(CardInstanceFactory cardInstanceFactory, Cards cards) {
      InitTestServiceDecorator initTestServiceDecorator = new InitTestServiceDecorator();
      initTestServiceDecorator.setCardInstanceFactory(cardInstanceFactory);
      initTestServiceDecorator.setCards(cards);
      return initTestServiceDecorator;
    }

    @Bean
    public LauncherTestGameController launcherTestGameController(LauncherGameResponseBuilder launcherGameResponseBuilder) {
      return new LauncherTestGameController(launcherGameResponseBuilder);
    }

    @Bean
    @Primary
    public PlayerInfoRetriever playerInfoRetriever() {
      return Mockito.mock(PlayerInfoRetriever.class);
    }
  }

  @SneakyThrows
  private void sleep5Secs() {
    Thread.sleep(3000);
  }

  private int getNumOfRetries() {
    try {
      return parseInt(System.getProperty("test.gameSetup.retries"));
    } catch (Exception e) {
      return 10;
    }
  }
}
