package application.scry;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Category(Regression.class)
public class ScryAbilityTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new ScryAbilityTest.InitTestServiceForTest());
  }

  @Test
  public void scryAbilityTest() {
    // Given Player Plays Get the Point
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Mountain")).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 3).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Get the Point")).select();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Feral Maaka")).click();

    // And Opponent accepts it
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // When Player continue without scrying:
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then
    browser.player1().getMessageHelper().hasMessage("Cannot continue: SCRY");
    browser.player1().getMessageHelper().close();

    // TODO At this point the front-end knows the card.
    System.out.println();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Feral Maaka"));
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));

      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Get the Point"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
    }
  }
}
