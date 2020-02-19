package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.aa.mtg.game.MtgApplication;
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
import static com.aa.mtg.cardinstance.modifiers.TappedModifier.TAPPED;
import static com.aa.mtg.cards.sets.CoreSet2019.ACT_OF_TREASON;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CONCORDIA_PEGASUS;
import static com.aa.mtg.player.PlayerType.OPPONENT;
import static com.aa.mtg.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({GainControlCreatureTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class GainControlCreatureTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new GainControlCreatureTest.InitTestServiceForTest());
    }

    @Test
    public void gainControlCreature() {
        // When cast the sorcery
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 2).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(ACT_OF_TREASON).select();
        browser.player1().getStatusHelper().hasMessage("Select targets for Act of Treason.");
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).click();

        // Sorcery goes on the stack
        browser.player1().getStackHelper().containsExactly(ACT_OF_TREASON);

        // When opponent accepts
        browser.player2().getActionHelper().clickContinue();

        // Player1 now control the creature
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).isEmpty();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).containsExactly(CONCORDIA_PEGASUS);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).isNotTapped();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).doesNotHaveSummoningSickness();
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, ACT_OF_TREASON);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);

            addCardToNonCurrentPlayerBattlefield(gameStatus, CONCORDIA_PEGASUS);
            gameStatus.getNonCurrentPlayer().getBattlefield().getCards().get(0).getModifiers().setSummoningSickness(true);
            gameStatus.getNonCurrentPlayer().getBattlefield().getCards().get(0).getModifiers().setTapped(TAPPED);
        }
    }
}
