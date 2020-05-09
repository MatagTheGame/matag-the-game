package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.Main1Phase.M1;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Category(Regression.class)
public class CastNegativeAuraOnOpponentCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastNegativeAuraOnOpponentCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void castNegativeAuraOnOpponentCreature() {
    // When cast an enchantment aura on a creature with toughness higher than 2
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Dead Weight")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Dead Weight.");
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).target();

    // Enchantment goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Dead Weight"));

    // When opponent accepts enchantment
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then the attachment and its effect are on the battlefield
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(cards.get("Dead Weight"));
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("1/2");

    // When cast an enchantment aura on a creature with toughness equal 1
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Dead Weight")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Dead Weight.");
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Nest Robber")).target();

    // Enchantment goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Dead Weight"));

    // When opponent accepts enchantment
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then the creature immediately dies
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Dead Weight"));
    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Nest Robber"));
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Dead Weight"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Dead Weight"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Grazing Whiptail"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Nest Robber"));
    }
  }
}
