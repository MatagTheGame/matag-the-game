package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Category(Regression.class)
public class CombatLifelinkTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CombatLifelinkTest.InitTestServiceForTest());
  }

  @Test
  public void combatLifelink() {
    // When going to combat
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, PLAYER);

    // When attacking
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Charity Extractor")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinue();

    // Then
    browser.player1().getPhaseHelper().is(M2, PLAYER);
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(19);
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(21);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Charity Extractor"));
    }
  }
}
