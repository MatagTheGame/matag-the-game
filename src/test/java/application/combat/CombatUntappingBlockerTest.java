package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.combat.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

public class CombatUntappingBlockerTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CombatUntappingBlockerTest.InitTestServiceForTest());
  }

  @Test
  public void combatUntappingBlocker() {
    // Player1 attacks
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(BC, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Headwater Sentries")).click();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, OPPONENT);

    // Player2 before going to blocking untap its creature
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Forest"), 0).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Forest"), 1).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Forest"), 2).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Spidery Grasp")).select();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Ancient Brontodon")).click();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);

    // Now Player2 can block
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Ancient Brontodon")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Headwater Sentries"));
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Headwater Sentries"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Ancient Brontodon"));
      gameStatus.getNonCurrentPlayer().getBattlefield().search().withName("Ancient Brontodon").getCards().get(0).getModifiers().setTapped(true);
      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Spidery Grasp"));
    }
  }
}
