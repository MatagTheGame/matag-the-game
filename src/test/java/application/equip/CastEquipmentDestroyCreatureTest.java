package application.equip;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.player.PlayerType.PLAYER;

@Tag("RegressionTest")
public class CastEquipmentDestroyCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastEquipmentDestroyCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void castEquipmentDestroyCreature() {
    // When cast an artifact equipment
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Short Sword")).click();

    // Equipment goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Short Sword"));

    // When opponent accepts equipment
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then the attachment and its effect are on the battlefield
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Short Sword"));
    int shortSwordId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Short Sword")).getCardIdNumeric();

    // When equipping
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Short Sword")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Short Sword.");
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Prowling Caracal")).target();

    // Equip ability goes on the stack
    browser.player1().getStackHelper().containsAbility("Player1's Short Sword (" + shortSwordId + "): Equipped creature gets +1/+1.");

    // When opponent accepts the equip
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then the target creature is equipped
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).containsExactly(cards.get("Prowling Caracal"), cards.get("Short Sword"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Prowling Caracal")).hasPowerAndToughness("4/2");

    // Destroy the creature
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 4).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).select();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Prowling Caracal")).target();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Creature is in the graveyard
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Prowling Caracal"), cards.get("Legion's Judgment"));

    // Creature is still in the battlefield
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Short Sword"));
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Prowling Caracal"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Legion's Judgment"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Short Sword"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    }
  }
}
