package application.cast;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;

public class WheneverCastTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new WheneverCastTest.InitTestServiceForTest());
  }

  @Test
  public void wheneverACreatureEntersTheBattlefieldAbility() {
    // When playing Precision Bolt
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Precision Bolt")).select();
    browser.player1().getPlayerInfoHelper(OPPONENT).click();

    // Then Adeliz gets activated
    browser.player1().getPhaseHelper().is(M1, OPPONENT);
    int adelizId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Adeliz, the Cinder Wind")).getCardIdNumeric();
    browser.player1().getStackHelper().containsAbility("Player1's Adeliz, the Cinder Wind (" + adelizId + "): Wizards you control get +1/+1 until end of turn.");

    // When Adeliz triggered ability resolves
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);

    // Then Adeliz power is increased
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Adeliz, the Cinder Wind")).hasPowerAndToughness("3/3");

    // When Precision Bolt
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Opponent loses 3 life
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(17);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Adeliz, the Cinder Wind"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Precision Bolt"));
    }
  }
}
