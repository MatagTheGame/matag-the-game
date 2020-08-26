package application.cleanup;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import static com.matag.game.turn.phases.ending.CleanupPhase.CL;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

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
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(CL, PLAYER);
    browser.player1().getHandHelper(PLAYER).toHaveSize(9);

    // It stops asking to discard a card
    browser.player1().getStatusHelper().hasMessage("Choose a card to discard.");
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getMessageHelper().hasMessage("Cannot continue: DISCARD_A_CARD");
    browser.player1().getMessageHelper().close();

    // When player clicks on a card to discard
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Plains")).select();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Mountain")).click();

    // Then that card is discarded (down to 7)
    browser.getHandHelper(PLAYER).toHaveSize(7);
    browser.getGraveyardHelper(PLAYER).toHaveSize(2);
    browser.getGraveyardHelper(PLAYER).contains(cards.get("Plains"), cards.get("Mountain"));

    // Priority is finally passed
    browser.player1().getPhaseHelper().is(M1, OPPONENT);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      // Current Player
      addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"));

      // Non Current Player
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
    }
  }
}
