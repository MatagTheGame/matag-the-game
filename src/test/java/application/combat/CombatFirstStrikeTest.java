package application.combat;

import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.combat.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.combat.CombatDamagePhase.CD;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.combat.EndOfCombatPhase.EC;
import static com.matag.game.turn.phases.combat.FirstStrikePhase.FS;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;

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
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(BC, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(BC, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);

    // When attacking
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Youthful Knight")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, OPPONENT);

    // Stop to play instants
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);

    // And blocking
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Coral Merfolk")).declareAsBlocker();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DB, PLAYER);

    // Then stop to play instants before first strike
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);

    // Then stop to play instants after first strike, before combat damage and at end of combat
    browser.player2().getActionHelper().clickContinueAndExpectPhase(FS, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(FS, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(CD, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(CD, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(EC, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(EC, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);

    // Then only the non first strike creature dies
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
