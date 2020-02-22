package application.cleanup;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.aa.mtg.cards.Cards;
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

import static com.aa.mtg.player.PlayerType.OPPONENT;
import static com.aa.mtg.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.EndTurnPhase.ET;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({DiscardACardAtEndOfTurnIfMoreThanSevenTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class DiscardACardAtEndOfTurnIfMoreThanSevenTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    @Autowired
    private Cards cards;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new DiscardACardAtEndOfTurnIfMoreThanSevenTest.InitTestServiceForTest());
    }

    @Test
    public void discardACardAtEndOfTurnIfMoreThanSeven() {
        // When arriving to End of Turn with 9 cards
        browser.player1().getActionHelper().clickContinue();
        browser.player1().getActionHelper().clickContinue();
        browser.getHandHelper(PLAYER).toHaveSize(9);

        // It stops asking to discard a card
        browser.player1().getPhaseHelper().is(ET, PLAYER);
        browser.player1().getStatusHelper().hasMessage("Choose a card to discard.");
        browser.player1().getActionHelper().clickContinue();
        browser.player1().getMessageHelper().hasMessage("Choose a card to discard.");
        browser.player1().getMessageHelper().close();

        // When player clicks on a card to discard
        browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Plains")).click();

        // Then that card is discarded (down to 8)
        browser.player1().getHandHelper(PLAYER).doesNotContain(cards.get("Plains"));
        browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Plains"));
        browser.getHandHelper(PLAYER).toHaveSize(8);

        // It stops asking to discard a card
        browser.player1().getPhaseHelper().is(ET, PLAYER);
        browser.player1().getStatusHelper().hasMessage("Choose a card to discard.");

        // When player clicks on a card to discard
        browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Mountain")).click();

        // Then that card is discarded (down to 7)
        browser.player1().getHandHelper(PLAYER).doesNotContain(cards.get("Mountain"));
        browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Mountain"));
        browser.getHandHelper(PLAYER).toHaveSize(7);

        // Priority is finally passed
        browser.player1().getPhaseHelper().is(ET, OPPONENT);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            // Current Player
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
            addCardToCurrentPlayerHand(gameStatus, cards.get("Plains"));
            addCardToCurrentPlayerHand(gameStatus, cards.get("Mountain"));
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
            addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
            addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"));

            // Non Current Player
            addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
        }
    }
}
