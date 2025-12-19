package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.*;
import static com.matag.game.turn.phases.combat.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.combat.CombatDamagePhase.CD;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.combat.EndOfCombatPhase.EC;
import static com.matag.game.turn.phases.ending.EndTurnPhase.ET;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Tag("RegressionTests")
public class CombatIndestructibleTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CombatIndestructibleTest.InitTestServiceForTest());
  }

  @Test
  public void indestructible() {
    // When going to combat
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(BC, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(BC, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);

    // When declare attacker
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Nyxborn Courser")).declareAsAttacker();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Nyxborn Marauder")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, OPPONENT);

    // Declare blocker
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Nyxborn Courser")).select();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Nyxborn Brute")).declareAsBlocker();
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Nyxborn Marauder")).select();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Nyxborn Colossus")).declareAsBlocker();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DB, PLAYER);

    // Make a Stand
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Make a Stand")).click();
    browser.player1().getPhaseHelper().is(DB, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DB, PLAYER);
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(CD, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(EC, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);

    // Then
    browser.player1().getGraveyardHelper(PLAYER).containsExactly(cards.get("Make a Stand"));
    browser.player1().getGraveyardHelper(OPPONENT).containsExactly(cards.get("Nyxborn Brute"));

    // Finally indestructible still gets destroyed if toughness reaches 0
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M2, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(ET, OPPONENT);
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Disfigure")).select();
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Nyxborn Marauder")).target();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(ET, OPPONENT);
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Disfigure")).select();
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Nyxborn Marauder")).target();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(ET, OPPONENT);
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Nyxborn Marauder"));
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Nyxborn Courser"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Nyxborn Marauder"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Make a Stand"));


      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Disfigure"));
      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Disfigure"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Nyxborn Colossus"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Nyxborn Brute"));
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));
    }
  }
}
