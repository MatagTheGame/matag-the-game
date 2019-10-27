package application.cast;

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

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.CoreSet2020.DARK_REMEDY;
import static com.aa.mtg.cards.sets.CoreSet2020.ENGULFING_ERUPTION;
import static com.aa.mtg.cards.sets.GuildsOfRavnica.DOUSER_OF_LIGHTS;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastMultipleInstantsTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CastMultipleInstantsTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CastMultipleInstantsTest.InitTestServiceForTest());
    }

    @Test
    public void castInstantPoweringCreatureDuringCombat() {
        // When player 1 try to deal 5 damage to a creature player 2 owns
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 2).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 3).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(ENGULFING_ERUPTION).click();
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(DOUSER_OF_LIGHTS).target();
        browser.player1().getStackHelper().contains(ENGULFING_ERUPTION);

        // Then player 2 can cast a spell to reinforce its creature and don't let it die
        browser.player2().getPhaseHelper().is(M1, PLAYER);
        browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 0).tap();
        browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 1).tap();
        browser.player2().getHandHelper(PLAYER).getFirstCard(DARK_REMEDY).click();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(DOUSER_OF_LIGHTS).target();
        browser.player2().getStackHelper().contains(ENGULFING_ERUPTION, DARK_REMEDY);

        // Player 1 continues
        browser.player1().getPhaseHelper().is(M1, PLAYER);
        browser.player1().getActionHelper().clickContinue();
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(DOUSER_OF_LIGHTS).hasPowerAndToughness("5/8");
        browser.player1().getStackHelper().contains(ENGULFING_ERUPTION);
        browser.player1().getGraveyardHelper(OPPONENT).contains(DARK_REMEDY);

        // Players 2 continues
        browser.player2().getPhaseHelper().is(M1, PLAYER);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(DOUSER_OF_LIGHTS).hasPowerAndToughness("5/8");
        browser.player2().getActionHelper().clickContinue();
        browser.player2().getStackHelper().isEmpty();
        browser.player2().getGraveyardHelper(OPPONENT).contains(ENGULFING_ERUPTION);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, ENGULFING_ERUPTION);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);

            addCardToNonCurrentPlayerHand(gameStatus, DARK_REMEDY);
            addCardToNonCurrentPlayerBattlefield(gameStatus, DOUSER_OF_LIGHTS);
            addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
            addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
        }
    }
}
