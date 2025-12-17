package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Tag("RegressionTest")
public class WhenAttackTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new WhenAttackTest.InitTestServiceForTest());
  }

  @Test
  public void combatTrampleHaste() {
    // going to attack
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);

    // attacking with a creature with when attacks ability
    int brazenWolvesId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Brazen Wolves")).getCardIdNumeric();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Brazen Wolves")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);

    // attacking ability is on the stack
    browser.player1().getStackHelper().containsAbility("Player1's Brazen Wolves (" + brazenWolvesId + "): Gets +2/+0 until end of turn.");
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, OPPONENT);
    browser.player2().getStackHelper().containsAbility("Player1's Brazen Wolves (" + brazenWolvesId + "): Gets +2/+0 until end of turn.");
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);

    // continuing
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(16);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Brazen Wolves"));
    }
  }
}
