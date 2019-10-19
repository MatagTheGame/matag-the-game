package application.cast;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.GuildsOfRavnica.GIRD_FOR_BATTLE;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CONCORDIA_PEGASUS;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({PutPlusOneCountersOnCreaturesTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class PutPlusOneCountersOnCreaturesTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new PutPlusOneCountersOnCreaturesTest.InitTestServiceForTest());
    }

    @Test
    public void putPlusOneCountersOnCreaturesTest() {
        // When cast the sorcery targeting two creatures player control
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(GIRD_FOR_BATTLE).select();
        browser.player1().getStatusHelper().hasMessage("Select targets for Gird for Battle.");
        // TODO Antonio: should be able to mark selected targets individually
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).click();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 1).click();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).isTargeted();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 1).isTargeted();

        // Sorcery goes on the stack
        browser.player1().getStackHelper().containsExactly(GIRD_FOR_BATTLE);

        // When opponent accepts
        browser.player2().getActionHelper().clickContinue();

        // Then the counters are added on both creatures
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).hasPlus1Counters(1);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).hasPowerAndToughness("2/4");
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 1).hasPlus1Counters(1);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 1).hasPowerAndToughness("2/4");



        // When cast the sorcery targeting twice the same creature
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(GIRD_FOR_BATTLE).select();
        browser.player1().getStatusHelper().hasMessage("Select targets for Gird for Battle.");
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).click();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).click();

        // An error is displayed
        browser.getMessageHelper().hasMessage("Targets must be different.");
        browser.getMessageHelper().close();



        // When cast the sorcery targeting only one creature
        browser.player1().getHandHelper(PLAYER).getFirstCard(GIRD_FOR_BATTLE).select();
        browser.player1().getStatusHelper().hasMessage("Select targets for Gird for Battle.");
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).click();
        browser.player1().getActionHelper().clickContinue();

        // Sorcery goes on the stack
        browser.player1().getStackHelper().containsExactly(GIRD_FOR_BATTLE);

        // When opponent accepts
        browser.player2().getActionHelper().clickContinue();

        // Then the counter is added on the creature
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).hasPlus1Counters(2);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(CONCORDIA_PEGASUS, 0).hasPowerAndToughness("3/5");
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
