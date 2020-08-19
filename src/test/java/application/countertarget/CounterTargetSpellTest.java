package application.countertarget;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

public class CounterTargetSpellTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new InitTestServiceForTest());
  }

  @Test
  public void counterTargetSpellTest() {
    // Player1 casts a creature
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Concordia Pegasus")).click();

    // Player2 counters it
    browser.player2().getPhaseHelper().is(M1, PLAYER);
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 0).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 1).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 2).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Cancel")).select();
    browser.player2().getStackHelper().getFirstCard(cards.get("Concordia Pegasus")).click();

    // Player1 accepts it
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);

    // Both spells are off the stack
    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Cancel"));
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Concordia Pegasus"));
    browser.player1().getStackHelper().isEmpty();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).isEmpty();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Concordia Pegasus"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));

      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Cancel"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
    }
  }
}
