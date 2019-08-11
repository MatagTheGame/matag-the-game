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
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.sets.CoreSet2019.BOGSTOMPER;
import static com.aa.mtg.cards.sets.CoreSet2019.EXCLUSION_MAGE;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureEntersTheBattlefieldReturnTargetToHandTest.InitGameTestConfiguration.class})
public class CreatureEntersTheBattlefieldReturnTargetToHandTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldReturnTargetToHandTest.InitTestServiceForTest());
    }

    @Test
    public void creatureEntersTheBattlefieldReturnTargetToHand() {
        // When Exclusion Mage
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(EXCLUSION_MAGE).click();
        browser.player2().getActionHelper().clickContinue();

        // And player 1 select opponent creature to return
        browser.player1().getPhaseHelper().is(M1, PLAYER);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(BOGSTOMPER).click();
        browser.player2().getActionHelper().clickContinue();

        // Then
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).isEmpty();
        browser.player1().getHandHelper(OPPONENT).toHaveSize(1);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, EXCLUSION_MAGE);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);

            addCardToNonCurrentPlayerBattlefield(gameStatus, BOGSTOMPER);
        }
    }
}
