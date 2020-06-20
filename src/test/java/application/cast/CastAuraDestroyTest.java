package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.Main1Phase.M1;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

public class CastAuraDestroyTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastAuraDestroyTest.InitTestServiceForTest());
  }

  @Test
  public void castAuraDestroy() {
    // Player1 casts 4 auras
    castArcaneFlight(0, "2/4");
    castArcaneFlight(1, "3/5");
    castArcaneFlight(2, "4/6");

    // Player1 continues
    browser.player1().getActionHelper().clickContinueAndExpectPhase(BC, OPPONENT);

    // When Player2 destroys an enchantment
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Invoke the Divine")).select();
    browser.player2().getStatusHelper().hasMessage("Select targets for Invoke the Divine.");
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Arcane Flight")).target();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(BC, OPPONENT);

    // The enchantments and the instant are in the graveyard
    browser.player2().getGraveyardHelper(PLAYER).contains(cards.get("Invoke the Divine"));
    browser.player2().getGraveyardHelper(OPPONENT).contains(cards.get("Arcane Flight"));
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).hasPowerAndToughness("3/5");
    browser.player2().getPlayerInfoHelper(PLAYER).toHaveLife(24);


    // When Player2 destroys the creature
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 2).tap();
    browser.player2().getHandHelper(PLAYER).getFirstCard(cards.get("Murder")).select();
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).target();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(BC, OPPONENT);

    // Creature and its enchantments are in the graveyard
    browser.player2().getGraveyardHelper(PLAYER).contains(cards.get("Invoke the Divine"), cards.get("Murder"));
    browser.player2().getGraveyardHelper(OPPONENT).contains(cards.get("Concordia Pegasus"), cards.get("Arcane Flight"), cards.get("Arcane Flight"), cards.get("Arcane Flight"), cards.get("Arcane Flight"));
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).isEmpty();
  }

  private void castArcaneFlight(int indexOfLandToTap, String powerAndToughness) {
    // When casting Arcane Flight
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), indexOfLandToTap).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Arcane Flight")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Arcane Flight.");
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).target();

    // Enchantment goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Arcane Flight"));

    // When opponent accepts enchantment
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then the attachment and its effect are on the battlefield
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Arcane Flight"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).hasPowerAndToughness(powerAndToughness);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Arcane Flight"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Arcane Flight"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Arcane Flight"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));

      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Invoke the Divine"));
      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Murder"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
    }
  }
}
