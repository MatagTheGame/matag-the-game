package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({PlayLandTest.InitGameTestConfiguration.class})
public class PlayLandTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new PlayLandTest.InitTestServiceForTest());
    }

    @Test
    public void playLand() {
        // When play first land
        browser.player1().getHandHelper(PLAYER).getFirstCard(ISLAND).click();

        // Then battlefields contain land
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).containsExactly(ISLAND);
        browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).containsExactly(ISLAND);

        // Hand is empty
        browser.player1().getHandHelper(PLAYER).toHaveSize(1);
        browser.player2().getHandHelper(OPPONENT).toHaveSize(1);

        // When play second land
        browser.player1().getHandHelper(PLAYER).getFirstCard(ISLAND).click();

        // Then error is displayed
        browser.player1().getMessageHelper().hasMessage("You already played a land this turn.");
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
        }
    }
}
