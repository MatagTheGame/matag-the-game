package application.counters;

import static application.browser.BattlefieldHelper.COMBAT_LINE;
import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.cards.ability.type.AbilityType.MENACE;
import static com.matag.game.turn.phases.combat.CombatDamagePhase.CD;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
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
public class PutAKeywordCounterTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new PutAKeywordCounterTest.InitTestServiceForTest());
  }

  @Test
  public void putAKeywordCounterOnCreatureTest() {
    // When cast Blood Curdle
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Blood Curdle")).select();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Catacomb Crocodile")).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Catacomb Crocodile")).target();

    // And opponent accepts
    browser.player2().getPhaseHelper().is(M1, PLAYER);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Blood Curdle"));
    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Catacomb Crocodile"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Catacomb Crocodile")).hasKeywordCounters(MENACE);

    // When attacking
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);
    int attackingCreature = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Catacomb Crocodile")).getCardIdNumeric();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Catacomb Crocodile")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);

    // When attempting to block
    int blockingCreature = browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Catacomb Crocodile")).getCardIdNumeric();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Catacomb Crocodile")).declareAsBlocker();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);

    // Then cannot as the opponent attacker as menace
    browser.player2().getMessageHelper().hasMessage("\"" + blockingCreature + " - Catacomb Crocodile" + "\" cannot block \"" + attackingCreature + " - Catacomb Crocodile\" alone as it has menace.");
    browser.player2().getMessageHelper().close();

    // So need to remove the blocker continue and lose
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Catacomb Crocodile")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(CD, PLAYER);
    browser.player2().getMessageHelper().hasMessage("Player1 Win! Go back to admin.");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      gameStatus.getPlayer1().setLife(1);
      gameStatus.getPlayer2().setLife(1);

      addCardToCurrentPlayerHand(gameStatus, cards.get("Blood Curdle"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Catacomb Crocodile"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Catacomb Crocodile"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Catacomb Crocodile"));
    }
  }
}
