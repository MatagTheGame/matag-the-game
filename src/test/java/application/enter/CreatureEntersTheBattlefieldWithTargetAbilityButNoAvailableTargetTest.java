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
import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.sets.CoreSet2020.FROST_LYNX;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureEntersTheBattlefieldWithTargetAbilityButNoAvailableTargetTest.InitGameTestConfiguration.class})
public class CreatureEntersTheBattlefieldWithTargetAbilityButNoAvailableTargetTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldWithTargetAbilityButNoAvailableTargetTest.InitTestServiceForTest());
    }

    @Test
    public void creatureEntersTheBattlefieldWithTargetAbilityTapDoesNotUntapTest() {
        // When Playing Frost Lynx
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(FROST_LYNX).click();
        browser.player2().getActionHelper().clickContinue();

        // Player 1 continue without targets are nothing can be targeted
        browser.player1().getActionHelper().clickContinue();
        browser.player1().getPhaseHelper().is(M1, PLAYER);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, FROST_LYNX);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerLibrary(gameStatus, ISLAND);

            addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);
        }
    }
}
