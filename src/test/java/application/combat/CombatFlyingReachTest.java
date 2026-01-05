package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.browser.CardHelper;
import com.matag.cards.Cards;
import application.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.COMBAT_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.combat.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.combat.DeclareBlockersPhase.DB;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Tag("RegressionTests")
public class CombatFlyingReachTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CombatFlyingReachTest.InitTestServiceForTest());
  }

  @Test
  public void combatFlyingReach() {
    // When going to combat
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);

    // creature with flying should have the correct class
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).hasFlying();

    // When declare attacker
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).declareAsAttacker();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).declareAsAttacker();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);

    // Declare blocker
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getCard(cards.get("Air Elemental"), 0).click();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).click();

    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getCard(cards.get("Air Elemental"), 1).click();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).click();

    CardHelper airElemental = browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getCard(cards.get("Air Elemental"), 2);
    airElemental.click();
    CardHelper ancientBrontodon = browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Ancient Brontodon"));
    ancientBrontodon.click();
    browser.player2().getMessageHelper().hasMessage("\"" + ancientBrontodon.getCardIdNumeric() + " - Ancient Brontodon\" cannot block \"" + airElemental.getCardIdNumeric() + " - Air Elemental\" as it has flying.");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Air Elemental"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Air Elemental"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Air Elemental"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Air Elemental"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Grazing Whiptail"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Ancient Brontodon"));
    }
  }
}
