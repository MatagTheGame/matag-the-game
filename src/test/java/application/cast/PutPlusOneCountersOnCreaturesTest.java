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
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.GuildsOfRavnica.GIRD_FOR_BATTLE;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CONCORDIA_PEGASUS;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({PutPlusOneCountersOnCreaturesTest.InitGameTestConfiguration.class})
public class PutPlusOneCountersOnCreaturesTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new PutPlusOneCountersOnCreaturesTest.InitTestServiceForTest());
    }

    @Test
    public void putPlusOneCountersOnCreaturesTest() {
        // When cast the sorcery
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(GIRD_FOR_BATTLE).select();
        browser.player1().getStatusHelper().hasMessage("Select targets for Gird for Battle.");
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).click();

        // Sorcery goes on the stack
        browser.player1().getStackHelper().containsExactly(GIRD_FOR_BATTLE);

        // When opponent accepts
        browser.player2().getActionHelper().clickContinue();

        // Then the counter is added.
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).hasPlus1Counters(1);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).hasPowerAndToughness("2/4");
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, GIRD_FOR_BATTLE);
            addCardToCurrentPlayerHand(gameStatus, GIRD_FOR_BATTLE);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);

            addCardToCurrentPlayerBattlefield(gameStatus, CONCORDIA_PEGASUS);
            addCardToCurrentPlayerBattlefield(gameStatus, CONCORDIA_PEGASUS);
        }
    }
}
