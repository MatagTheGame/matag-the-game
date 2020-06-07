package application.counters;

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
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Category(Regression.class)
public class PutAKeywordCounter extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new PutAKeywordCounter.InitTestServiceForTest());
  }

  @Test
  public void putPlusOneCountersOnCreaturesTest() {
    // When cast Blood Curdle
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Blood Curdle")).select();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).select();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).select();

    System.out.println("");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      gameStatus.getPlayer1().setLife(1);
      gameStatus.getPlayer2().setLife(1);

      addCardToCurrentPlayerHand(gameStatus, cards.get("Blood Curdle"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"));
    }
  }
}
