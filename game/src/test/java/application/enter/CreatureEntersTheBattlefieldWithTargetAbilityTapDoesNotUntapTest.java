package application.enter;

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
import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.sets.CoreSet2020.CANOPY_SPIDER;
import static com.aa.mtg.cards.sets.CoreSet2020.FROST_LYNX;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureEntersTheBattlefieldWithTargetAbilityTapDoesNotUntapTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CreatureEntersTheBattlefieldWithTargetAbilityTapDoesNotUntapTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldWithTargetAbilityTapDoesNotUntapTest.InitTestServiceForTest());
    }

    @Test
    public void creatureEntersTheBattlefieldWithTargetAbilityTapDoesNotUntapTest() {
        // When Playing Frost Lynx
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(FROST_LYNX).click();
        browser.player2().getActionHelper().clickContinue();

        // Player 1 cannot resolve without choosing a target if there are targets available
        browser.player1().getActionHelper().clickContinue();
        int frostLynxId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(FROST_LYNX).getCardIdNumeric();
        browser.player1().getMessageHelper().hasMessage("\"" + frostLynxId  + " - Frost Lynx\" requires a valid target.");
        browser.player1().getMessageHelper().close();

        // Player 1 chooses a target
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(CANOPY_SPIDER).click();
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(CANOPY_SPIDER).isTargeted();
        browser.player1().getPhaseHelper().is(M1, OPPONENT);

        // Player 2 just continues
        browser.player2().getPhaseHelper().is(M1, PLAYER);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CANOPY_SPIDER).isTargeted();
        browser.player2().getActionHelper().clickContinue();

        // Player 1 has priority again and target is tapped
        browser.player1().getPhaseHelper().is(M1, PLAYER);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(CANOPY_SPIDER).isTappedDoesNotUntapNextTurn();

        // Next turn target is still tapped
        browser.player1().getActionHelper().clickContinue();
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getPhaseHelper().is(M1, PLAYER);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CANOPY_SPIDER).isTapped();
        browser.player2().getHandHelper(PLAYER).contains(FOREST);

        // Next next turn target is still tapped
        browser.player2().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getActionHelper().clickContinue();
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getPhaseHelper().is(M1, PLAYER);
        browser.player1().getHandHelper(PLAYER).contains(ISLAND);

        // Next next next turn target is untapped
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CANOPY_SPIDER).isNotTapped();
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, FROST_LYNX);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerLibrary(gameStatus, ISLAND);

            addCardToNonCurrentPlayerBattlefield(gameStatus, CANOPY_SPIDER);
            addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);
        }
    }
}
