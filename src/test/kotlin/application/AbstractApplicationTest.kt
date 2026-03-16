package application

import application.AbstractApplicationTest.InitGameTestConfiguration
import application.browser.MatagBrowser
import com.matag.adminentities.DeckInfo
import com.matag.adminentities.PlayerInfo
import com.matag.cards.Cards
import com.matag.game.MatagGameApplication
import com.matag.game.adminclient.AdminClient
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.launcher.LauncherGameResponseBuilder
import com.matag.game.launcher.LauncherTestGameController
import com.matag.game.security.SecurityToken
import com.matag.game.status.GameStatusRepository
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatcher
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.argThat
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [MatagGameApplication::class], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(InitGameTestConfiguration::class)
@ActiveProfiles("test")
abstract class AbstractApplicationTest {
    @LocalServerPort private val port = 0

    @Autowired protected lateinit var adminClient: AdminClient
    @Autowired protected lateinit var gameStatusRepository: GameStatusRepository
    @Autowired protected lateinit var cardInstanceFactory: CardInstanceFactory
    @Autowired protected lateinit var cards: Cards

    protected lateinit var browser: MatagBrowser

    abstract fun setupGame()

    @BeforeEach
    fun setup() {
        given(adminClient.getPlayerInfo(argThat(SecurityTokenMatcher("1"))))
            .willReturn(PlayerInfo("Player1"))
        given(adminClient.getPlayerInfo(argThat(SecurityTokenMatcher("2"))))
            .willReturn(PlayerInfo("Player2"))

        given(adminClient.getDeckInfo(any())).willReturn(
            DeckInfo(
                listOf(
                    cards.get("Plains"),
                    cards.get("Plains"),
                    cards.get("Plains"),
                    cards.get("Plains"),
                    cards.get("Plains"),
                    cards.get("Plains"),
                    cards.get("Plains")
                )
            )
        )

        setupGame()

        // When player1 joins the game is waiting for opponent
        browser = MatagBrowser(port)
        browser.getMessageHelper().hasMessage("Waiting for opponent...")

        // When player2 joins the game both players see the table with the cards
        LOGGER.info("TOKEN_PLAYER2: {}", LauncherTestGameController.TEST_ADMIN_TOKEN.get())
        browser.openSecondTab()

        browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveName()
        browser.player2().getPlayerInfoHelper(PlayerType.PLAYER).toHaveName()

        // Make sure player1 is Player1 and player2 is Player2
        LOGGER.info(
            "player1.name: {};   player2.name: {}",
            browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).playerName,
            browser.player2().getPlayerInfoHelper(PlayerType.PLAYER).playerName
        )
        if (browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).playerName == "Player2") {
            browser.swapTabs()
        }
        browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveName("Player1")
        browser.player2().getPlayerInfoHelper(PlayerType.PLAYER).toHaveName("Player2")

        // Message disappears
        browser.player1().getMessageHelper().hasNoMessage()
        browser.player2().getMessageHelper().hasNoMessage()

        while (true) {
            try {
                browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
                break
            } catch (_: Exception) {
                if (browser.player1().getPhaseHelper().priority == PlayerType.PLAYER && browser.player2().getPhaseHelper().priority == PlayerType.OPPONENT) {
                    browser.player1().getStatusHelper().hasMessage("Play any instant or ability or continue (SPACE).")
                    browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...")
                    browser.player1().getActionHelper().clickContinue()
                } else if (browser.player1().getPhaseHelper().priority == PlayerType.OPPONENT && browser.player2().getPhaseHelper().priority == PlayerType.PLAYER) {
                    browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...")
                    browser.player2().getStatusHelper().hasMessage("Play any instant or ability or continue (SPACE).")
                    browser.player2().getActionHelper().clickContinue()
                }
            }
        }

        // Status is
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).")
        browser.player2().getPhaseHelper().matches(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...")
    }

    @AfterEach
    fun cleanup() {
        //browser.dumpContent();
        browser.close()

        Mockito.reset(adminClient)
        gameStatusRepository.clear()
        LauncherTestGameController.TEST_ADMIN_TOKEN.set(0)
        LOGGER.info("Test cleaned up.")
    }

    @Configuration
    open class InitGameTestConfiguration {

        @Bean
        open fun launcherTestGameController(launcherGameResponseBuilder: LauncherGameResponseBuilder): LauncherTestGameController {
            return LauncherTestGameController(launcherGameResponseBuilder)
        }

        @Bean
        @Primary
        open fun adminClient(): AdminClient? {
            return mock(AdminClient::class.java)
        }
    }

    class SecurityTokenMatcher(private val adminToken: String) : ArgumentMatcher<SecurityToken?> {
        override fun matches(argument: SecurityToken?): Boolean {
            return argument != null && adminToken == argument.adminToken
        }
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(AbstractApplicationTest::class.java)
    }
}
