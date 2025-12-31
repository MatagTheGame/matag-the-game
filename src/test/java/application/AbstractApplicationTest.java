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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.matag.game.launcher.LauncherTestGameController.TEST_ADMIN_TOKEN;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MatagGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({AbstractApplicationTest.InitGameTestConfiguration.class})
@ActiveProfiles("test")
public abstract class AbstractApplicationTest {
  private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationTest.class);

  @LocalServerPort
  private int port;

  protected MatagBrowser browser;

  @Autowired
  private AdminClient adminClient;

  @Autowired
  private GameStatusRepository gameStatusRepository;

  @Autowired
  private Cards cards;

  public abstract void setupGame();

  @BeforeEach
  public void setup() {
      given(adminClient.getPlayerInfo(argThat(new SecurityTokenMatcher("1")))).willReturn(new PlayerInfo("Player1"));
      given(adminClient.getPlayerInfo(argThat(new SecurityTokenMatcher("2")))).willReturn(new PlayerInfo("Player2"));

      given(adminClient.getDeckInfo(any())).willReturn(new DeckInfo(List.of(
              cards.get("Plains"),
              cards.get("Plains"),
              cards.get("Plains"),
              cards.get("Plains"),
              cards.get("Plains"),
              cards.get("Plains"),
              cards.get("Plains")
      )));

    setupGame();

    // When player1 joins the game is waiting for opponent
    browser = new MatagBrowser(port);
    browser.getMessageHelper().hasMessage("Waiting for opponent...");

    // When player2 joins the game both players see the table with the cards
    LOGGER.info("TOKEN_PLAYER2: {}", TEST_ADMIN_TOKEN.get());
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

    while (true) {
      try {
        browser.player1().getPhaseHelper().is(M1, PLAYER);
        break;

      } catch (Exception e) {
        if (browser.player1().getPhaseHelper().getPriority().equals(PLAYER) && browser.player2().getPhaseHelper().getPriority().equals(OPPONENT)) {
          browser.player1().getStatusHelper().hasMessage("Play any instant or ability or continue (SPACE).");
          browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
          browser.player1().getActionHelper().clickContinue();

        } else if (browser.player1().getPhaseHelper().getPriority().equals(OPPONENT) && browser.player2().getPhaseHelper().getPriority().equals(PLAYER)) {
          browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
          browser.player2().getStatusHelper().hasMessage("Play any instant or ability or continue (SPACE).");
          browser.player2().getActionHelper().clickContinue();
        }
      }
    }

    // Status is
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
    browser.player2().getPhaseHelper().is(M1, OPPONENT);
    browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
  }

  @AfterEach
  public void cleanup() {
    //browser.dumpContent();
    browser.close();

    Mockito.reset(adminClient);
    gameStatusRepository.clear();
    TEST_ADMIN_TOKEN.set(0);
    LOGGER.info("Test cleaned up.");
  }

  @Configuration
  public static class InitGameTestConfiguration {
    @Bean
    public InitTestServiceDecorator initTestServiceDecorator(CardInstanceFactory cardInstanceFactory, Cards cards) {
      var initTestServiceDecorator = new InitTestServiceDecorator();
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
