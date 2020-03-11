package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.MatagGameApplication;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.*;
import static com.matag.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.matag.game.turn.phases.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.Main2Phase.M2;
import static com.matag.game.turn.phases.UpkeepPhase.UP;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastInstantPoweringCreatureDuringCombatTest.InitGameTestConfiguration.class})
public class CastInstantPoweringCreatureDuringCombatTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastInstantPoweringCreatureDuringCombatTest.InitTestServiceForTest());
  }

  @Test
  public void castInstantPoweringCreatureDuringCombat() {
    // When player one attack thinking to win the fight
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getPhaseHelper().is(BC, PLAYER);
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Bastion Enforcer")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinue();

    // And player 2 plays the game by blocking
    browser.player2().getActionHelper().clickContinue();
    browser.player2().getPhaseHelper().is(DB, PLAYER);
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Bartizan Bats")).declareAsBlocker();
    browser.player2().getActionHelper().clickContinue();

    // And player 1 don't play anything
    browser.player1().getPhaseHelper().is(AB, PLAYER);
    browser.player1().getActionHelper().clickContinue();

    // Then player 2 can pump up its creature
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player2().getPhaseHelper().is(AB, PLAYER);
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Dark Remedy")).select();
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Bartizan Bats")).target();

    // And spell goes on the stack
    browser.player2().getStackHelper().contains(cards.get("Dark Remedy"));
    browser.player1().getStackHelper().contains(cards.get("Dark Remedy"));

    // When player 1 continue
    browser.player1().getPhaseHelper().is(AB, PLAYER);
    browser.player1().getActionHelper().clickContinue();

    // Then spell is resolved from the stack and creature gets the +1/+3
    browser.player1().getStackHelper().isEmpty();
    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Dark Remedy"));
    browser.player1().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Bartizan Bats")).hasPowerAndToughness("4/4");
    browser.player1().getPhaseHelper().is(AB, OPPONENT);
    browser.player2().getStackHelper().isEmpty();
    browser.player2().getGraveyardHelper(PLAYER).contains(cards.get("Dark Remedy"));
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Bartizan Bats")).hasPowerAndToughness("4/4");
    browser.player2().getPhaseHelper().is(AB, PLAYER);

    // When player 2 continues
    browser.player2().getActionHelper().clickContinue();

    // Then combat is ended
    browser.player2().getPhaseHelper().is(M2, OPPONENT);
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Bartizan Bats")).hasDamage(3);
    browser.player2().getGraveyardHelper(OPPONENT).contains(cards.get("Bastion Enforcer"));

    // When moving to next turn
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();

    // Then extra power and toughness is cleaned up
    browser.player2().getPhaseHelper().is(UP, PLAYER);
    browser.getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Bartizan Bats")).hasPowerAndToughness("3/1");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Bastion Enforcer"));

      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Dark Remedy"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Bartizan Bats"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
    }
  }
}
