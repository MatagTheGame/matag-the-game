package application.cast;

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
import static com.matag.game.turn.phases.Main1Phase.M1;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Category(Regression.class)
public class CastMultipleInstantsTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastMultipleInstantsTest.InitTestServiceForTest());
  }

  @Test
  public void castInstantPoweringCreatureDuringCombat() {
    // When player 1 try to deal 5 damage to a creature player 2 owns
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 3).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Engulfing Eruption")).click();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Douser of Lights")).target();
    browser.player1().getStackHelper().contains(cards.get("Engulfing Eruption"));

    // Then player 2 can cast a spell to reinforce its creature and don't let it die
    browser.player2().getPhaseHelper().is(M1, PLAYER);
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Dark Remedy")).click();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Douser of Lights")).target();
    browser.player2().getStackHelper().contains(cards.get("Engulfing Eruption"), cards.get("Dark Remedy"));

    // Player 1 continues
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Douser of Lights")).hasPowerAndToughness("5/8");
    browser.player1().getStackHelper().contains(cards.get("Engulfing Eruption"));
    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Dark Remedy"));

    // Players 2 continues
    browser.player2().getPhaseHelper().is(M1, PLAYER);
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Douser of Lights")).hasPowerAndToughness("5/8");
    browser.player2().getActionHelper().clickContinue();
    browser.player2().getStackHelper().isEmpty();
    browser.player2().getGraveyardHelper(OPPONENT).contains(cards.get("Engulfing Eruption"));
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Engulfing Eruption"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));

      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Dark Remedy"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Douser of Lights"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
    }
  }
}
