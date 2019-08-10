package application.enter;

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
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.CoreSet2019.DIREGRAF_GHOUL;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureEntersTheBattlefieldTappedTest.InitGameTestConfiguration.class})
public class CreatureEntersTheBattlefieldTappedTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldTappedTest.InitTestServiceForTest());
    }

    @Test
    public void creatureEntersTheBattlefieldTapped() {
        // When Playing Diregraf Ghoul
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(SWAMP).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(DIREGRAF_GHOUL).click();
        browser.player2().getActionHelper().clickContinue();

        // Then it enters tapped
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(DIREGRAF_GHOUL).isTapped();
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, DIREGRAF_GHOUL);
            addCardToCurrentPlayerBattlefield(gameStatus, SWAMP);
        }
    }
}
