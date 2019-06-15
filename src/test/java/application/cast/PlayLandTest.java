package application.cast;

import application.AbstractApplicationTest;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.CardHelper.cardNames;
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({PlayLandTest.InitGameTestConfiguration.class})
public class PlayLandTest extends AbstractApplicationTest {
    @Test
    public void playLand() {
        // When play first land
        player1.getHandHelper(PLAYER).getFirstCard(ISLAND).click();

        // Then battlefields contain land
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).containsExactly(cardNames(ISLAND));
        player2.getBattlefieldHelper(OPPONENT, FIRST_LINE).containsExactly(cardNames(ISLAND));

        // Hand is empty
        player1.getHandHelper(PLAYER).toHaveSize(1);
        player2.getHandHelper(OPPONENT).toHaveSize(1);

        // When play second land
        player1.getHandHelper(PLAYER).getFirstCard(ISLAND).click();

        // Then error is displayed
        player1.getMessageHelper().hasMessage("You already played a land this turn.");
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
