package application.cast;

import application.browser.MtgBrowser;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.CardHelper.cardNames;
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({playLandTest.InitGameTestConfiguration.class})
public class playLandTest {
    @LocalServerPort
    private int port;

    @Test
    public void playLand() {
        // Given
        MtgBrowser player1 = new MtgBrowser(port);
        MtgBrowser player2 = new MtgBrowser(port);

        // When play first land
        player1.getHandHelper(PLAYER).clickCard(ISLAND);

        // Then battlefields are
        player1.getBattlefieldHelper(PLAYER).battlefieldContainsExactly(cardNames(ISLAND));
        player2.getBattlefieldHelper(OPPONENT).battlefieldContainsExactly(cardNames(ISLAND));

        // When play second land
        player1.getHandHelper(PLAYER).clickCard(ISLAND);

        // Then error is displayed
        player1.getMessageHelper().hasMessage("You already played a land this turn.");

        // Close browsers
        player1.close();
        player2.close();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService(GameStatusUpdaterService gameStatusUpdaterService) {
            return new InitTestService(gameStatusUpdaterService) {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerHand(gameStatus, ISLAND);
                    addCardToCurrentPlayerHand(gameStatus, ISLAND);
                }
            };
        }
    }
}
