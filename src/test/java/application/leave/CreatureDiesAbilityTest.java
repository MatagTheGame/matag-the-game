package application.leave;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.matag.game.turn.phases.BeginCombatPhase.BC;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static java.util.Collections.singletonList;

public class CreatureDiesAbilityTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CreatureDiesAbilityTest.InitTestServiceForTest());
  }

  @Test
  public void creatureDiesAbility() {
    int firstGoblinId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Goblin Assault Team"), 0).getCardIdNumeric();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Goblin Assault Team"), 1).getCardIdNumeric();

    // When opponent kills 1 goblin
    browser.player1().getActionHelper().clickContinueAndExpectPhase(BC, OPPONENT);
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 2).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Murder")).click();
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Goblin Assault Team")).click();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(BC, OPPONENT);

    // Then put +1/+1 counter is triggered
    browser.player1().getStackHelper().containsAbilitiesExactly(singletonList("Player1's Goblin Assault Team (" + firstGoblinId + "): That targets get 1 +1/+1 counters."));

    // When clicking on the other goblin
    browser.player2().getActionHelper().clickContinueAndExpectPhase(BC, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Goblin Assault Team")).target();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(BC, PLAYER);

    // Then that goblin gets a counter
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Goblin Assault Team")).hasPlus1Counters(1);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Goblin Assault Team")).hasPowerAndToughness("5/2");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Goblin Assault Team"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Goblin Assault Team"));

      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Murder"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
    }
  }
}
