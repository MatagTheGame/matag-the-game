package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.MatagGameApplication;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.matag.game.turn.phases.AfterFirstStrikePhase.AF;
import static com.matag.game.turn.phases.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CombatFirstStrikeTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CombatFirstStrikeTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CombatFirstStrikeTest.InitTestServiceForTest());
  }

  @Test
  public void combatFirstStrike() {
    // Stops to play instants
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(BC, OPPONENT);
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, PLAYER);

    // When attacking
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Youthful Knight")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinue();

    // Stop to play instants
    browser.player1().getPhaseHelper().is(DA, OPPONENT);
    browser.player2().getActionHelper().clickContinue();
    browser.player2().getPhaseHelper().is(DB, PLAYER);

    // And blocking
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Coral Merfolk")).declareAsBlocker();
    browser.player2().getActionHelper().clickContinue();

    // Then stop to play instants before first strike
    browser.player1().getPhaseHelper().is(AB, PLAYER);
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(AB, OPPONENT);
    browser.player2().getActionHelper().clickContinue();

    // Then stop to play instants after first strike before combat damage
    browser.player1().getPhaseHelper().is(AF, PLAYER);
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(AF, OPPONENT);
    browser.player2().getActionHelper().clickContinue();

    // Then only the non first stike creature dies
    browser.player1().getPhaseHelper().is(M2, PLAYER);
    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Coral Merfolk"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Youthful Knight"));
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Youthful Knight"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Bladebrand"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Coral Merfolk"));
      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Bladebrand"));
    }
  }
}
