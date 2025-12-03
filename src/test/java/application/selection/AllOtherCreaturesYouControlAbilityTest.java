package application.selection;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;

public class AllOtherCreaturesYouControlAbilityTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new AllOtherCreaturesYouControlAbilityTest.InitTestServiceForTest());
  }

  @Test
  public void allOtherCreaturesAbilityTest() {
    // Creatures have basic power
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("3/4");
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("3/4");

    // When Benalish Marshal is on the battlefield
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Benalish Marshal")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Players Creatures only have increased power
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Benalish Marshal")).hasPowerAndToughness("3/3");
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("4/5");
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("3/4");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Benalish Marshal"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Enforcer Griffin"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Enforcer Griffin"));
    }
  }
}
