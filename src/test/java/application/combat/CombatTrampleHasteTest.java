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

import static application.browser.BattlefieldHelper.*;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Category(Regression.class)
public class CombatTrampleHasteTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CombatTrampleHasteTest.InitTestServiceForTest());
  }

  @Test
  public void combatTrampleHaste() {
    // Playing card with trample haste
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 3).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 4).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Charging Monstrosaur")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // When going to combat
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);

    // When declare attacker
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Charging Monstrosaur")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);

    // Declare blocker
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Charging Monstrosaur")).select();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Huatli's Snubhorn")).declareAsBlocker();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);

    // Then
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(17);

    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Huatli's Snubhorn"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Charging Monstrosaur")).hasDamage(2);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Charging Monstrosaur"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Huatli's Snubhorn"));
    }
  }
}
