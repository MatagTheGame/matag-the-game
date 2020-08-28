package application.counters;

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

public class PutPlusOneCountersOnCreaturesTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new PutPlusOneCountersOnCreaturesTest.InitTestServiceForTest());
  }

  @Test
  public void putPlusOneCountersOnCreaturesTest() {
    // When cast Gird for Battle targeting two creatures player control
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Gird for Battle")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Gird for Battle.");
    // TODO Antonio: should be able to mark selected targets individually
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 0).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 1).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 0).isTargeted();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 1).isTargeted();

    // Sorcery goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Gird for Battle"));

    // When opponent accepts
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then the counters are added on both creatures
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 0).hasPlus1Counters(1);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 0).hasPowerAndToughness("2/4");
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 1).hasPlus1Counters(1);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 1).hasPowerAndToughness("2/4");


    // When cast the sorcery targeting only one creature
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Gird for Battle")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Gird for Battle.");
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 0).click();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);

    // Sorcery goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Gird for Battle"));

    // When opponent accepts
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then the counter is added on the creature
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 0).hasPlus1Counters(2);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Concordia Pegasus"), 0).hasPowerAndToughness("3/5");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Gird for Battle"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Gird for Battle"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"));
    }
  }
}
