package application.cleanup;

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

import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.EndTurnPhase.ET;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({DiscardACardAtEndOfTurnIfMoreThanSevenTest.InitGameTestConfiguration.class})
public class DiscardACardAtEndOfTurnIfMoreThanSevenTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

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
        browser.player1().getHandHelper(PLAYER).getFirstCard(PLAINS).click();

        // Then that card is discarded (down to 8)
        browser.player1().getHandHelper(PLAYER).doesNotContain(PLAINS);
        browser.player1().getGraveyardHelper(PLAYER).contains(PLAINS);
        browser.getHandHelper(PLAYER).toHaveSize(8);

        // It stops asking to discard a card
        browser.player1().getPhaseHelper().is(ET, PLAYER);
        browser.player1().getStatusHelper().hasMessage("Choose a card to discard.");

        // When player clicks on a card to discard
        browser.player1().getHandHelper(PLAYER).getFirstCard(MOUNTAIN).click();

        // Then that card is discarded (down to 7)
        browser.player1().getHandHelper(PLAYER).doesNotContain(MOUNTAIN);
        browser.player1().getGraveyardHelper(PLAYER).contains(MOUNTAIN);
        browser.getHandHelper(PLAYER).toHaveSize(7);

        // Priority is finally passed
        browser.player1().getPhaseHelper().is(ET, OPPONENT);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            // Current Player
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
            addCardToCurrentPlayerHand(gameStatus, PLAINS);
            addCardToCurrentPlayerHand(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
            addCardToCurrentPlayerHand(gameStatus, ISLAND);
            addCardToCurrentPlayerLibrary(gameStatus, ISLAND);

            // Non Current Player
            addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
        }
    }
}
