package application;

import application.browser.MatagBrowser;
import com.matag.adminentities.DeckInfo;
import com.matag.adminentities.PlayerInfo;
import com.matag.cards.Cards;
import com.matag.game.MatagGameApplication;
import com.matag.game.adminclient.AdminClient;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.launcher.LauncherGameResponseBuilder;
import com.matag.game.launcher.LauncherTestGameController;
import com.matag.game.security.SecurityToken;
import com.matag.game.status.GameStatusRepository;
import lombok.SneakyThrows;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static com.matag.cards.properties.Color.WHITE;
import static com.matag.game.launcher.LauncherTestGameController.TEST_ADMIN_TOKEN;
import static com.matag.game.turn.phases.Main1Phase.M1;
import static com.matag.game.turn.phases.UpkeepPhase.UP;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({AbstractApplicationTest.InitGameTestConfiguration.class})
public abstract class AbstractApplicationTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationTest.class);

  @LocalServerPort
  private int port;

  protected MatagBrowser browser;

  @Autowired
  private AdminClient adminClient;

  @Autowired
  private GameStatusRepository gameStatusRepository;

  public abstract void setupGame();

  @Before
  public void setupRetrieverMocks() {
    given(adminClient.getPlayerInfo(argThat(new SecurityTokenMatcher("1")))).willReturn(new PlayerInfo("Player1"));
    given(adminClient.getPlayerInfo(argThat(new SecurityTokenMatcher("2")))).willReturn(new PlayerInfo("Player2"));

    given(adminClient.getDeckInfo(any())).willReturn(new DeckInfo(Set.of(WHITE)));
  }

  @Before
  public void setup() {
    setupGame();

    // When player1 joins the game is waiting for opponent
    browser = new MatagBrowser(port);
    browser.getMessageHelper().hasMessage("Waiting for opponent...");

    // When player2 joins the game both players see the table with the cards
    LOGGER.info("TOKEN_PLAYER2: " + TEST_ADMIN_TOKEN.get());
    browser.openSecondTab();

    browser.player1().getPlayerInfoHelper(PLAYER).toHaveName();
    browser.player2().getPlayerInfoHelper(PLAYER).toHaveName();

    // Make sure player1 is Player1 and player2 is Player2
    LOGGER.info("player1.name: {};   player2.name: {}", browser.player1().getPlayerInfoHelper(PLAYER).getPlayerName(), browser.player2().getPlayerInfoHelper(PLAYER).getPlayerName());
    if (browser.player1().getPlayerInfoHelper(PLAYER).getPlayerName().equals("Player2")) {
      browser.swapTabs();
    }
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveName("Player1");
    browser.player2().getPlayerInfoHelper(PLAYER).toHaveName("Player2");

    // Message disappears
    browser.player1().getMessageHelper().hasNoMessage();
    browser.player2().getMessageHelper().hasNoMessage();

    sleepABit();
    while (browser.player1().getPhaseHelper().getPhase().equals(UP)) {
      // UP is only active if player can play something
      if (browser.player1().getPhaseHelper().getPriority().equals(PLAYER)) {
        // Status and Phase are
        browser.player1().getPhaseHelper().is(UP, PLAYER);
        browser.player1().getStatusHelper().hasMessage("Play any instant or ability or continue (SPACE).");
        browser.player2().getPhaseHelper().is(UP, OPPONENT);
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");

        // Player1 continues
        browser.player1().getActionHelper().clickContinue();
        sleepABit();
      }

      // UP is only active if opponent can play something
      if (browser.player1().getPhaseHelper().getPriority().equals(OPPONENT)) {
        // Status and Phase are
        browser.player1().getPhaseHelper().is(UP, OPPONENT);
        browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
        browser.player2().getPhaseHelper().is(UP, PLAYER);
        browser.player2().getStatusHelper().hasMessage("Play any instant or ability or continue (SPACE).");

        // Player2 continues
        browser.player2().getActionHelper().clickContinue();
      }
    }

    // Status is
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
    browser.player2().getPhaseHelper().is(M1, OPPONENT);
    browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
  }

  @SneakyThrows
  private void sleepABit() {
    Thread.sleep(100);
  }

  @After
  public void closeBrowser() {
    //browser.dumpContent();
    browser.close();
  }

  @After
  public void cleanup() {
    Mockito.reset(adminClient);
    gameStatusRepository.clear();
    TEST_ADMIN_TOKEN.set(0);
    LOGGER.info("Test cleaned up.");
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
    public AdminClient adminClient() {
      return Mockito.mock(AdminClient.class);
    }
  }

  public static class SecurityTokenMatcher implements ArgumentMatcher<SecurityToken> {
    private final String adminToken;

    public SecurityTokenMatcher(String adminToken) {
      this.adminToken = adminToken;
    }

    @Override
    public boolean matches(SecurityToken argument) {
      return argument != null && adminToken.equals(argument.getAdminToken());
    }
  }
}
