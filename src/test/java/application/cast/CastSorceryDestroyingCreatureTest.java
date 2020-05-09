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
public class CastSorceryDestroyingCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastSorceryDestroyingCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void castSorceryDestroyingCreature() {
    // When clicking all lands
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 0).tap();

    // When click on a sorcery that requires target
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).click();

    // Then card is selected and message ask to choose a target
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).isSelected();
    browser.player1().getStatusHelper().hasMessage("Select targets for Legion's Judgment.");

    // When un-selecting the card
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).click();

    // Then card is unselected
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).isNotSelected();
    browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");

    // When click on a sorcery that requires target
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).click();

    // Then again card is selected and message ask to choose a target
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).isSelected();
    browser.player1().getStatusHelper().hasMessage("Select targets for Legion's Judgment.");

    // When clicking a wrong target
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Huatli's Snubhorn")).click();

    // The card is still on the hand selected
    browser.player1().getMessageHelper().hasMessage("Selected targets were not valid.");
    browser.player1().getMessageHelper().close();
    browser.player1().getMessageHelper().hasNoMessage();

    // When clicking again on the sorcery and on a valid target
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).click();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Colossal Dreadmaw")).click();

    // Then spell goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Legion's Judgment"));
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Colossal Dreadmaw")).isTargeted();
    browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    browser.player2().getStackHelper().containsExactly(cards.get("Legion's Judgment"));
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Colossal Dreadmaw")).isTargeted();
    browser.player2().getStatusHelper().hasMessage("Play any instant or ability or resolve the top spell in the stack (SPACE).");

    // And priority is passed to the opponent
    browser.player1().getActionHelper().cannotContinue();
    browser.player1().getPhaseHelper().is(M1, OPPONENT);
    browser.player2().getActionHelper().canContinue();
    browser.player2().getPhaseHelper().is(M1, PLAYER);

    // When player 2 resolves the spell
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then the creature is destroyed
    browser.player1().getStackHelper().isEmpty();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).doesNotContain(cards.get("Colossal Dreadmaw"));
    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Colossal Dreadmaw"));
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Legion's Judgment"));
    browser.player2().getStackHelper().isEmpty();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).doesNotContain(cards.get("Colossal Dreadmaw"));
    browser.player2().getGraveyardHelper(PLAYER).contains(cards.get("Colossal Dreadmaw"));
    browser.player2().getGraveyardHelper(OPPONENT).contains(cards.get("Legion's Judgment"));

    // And priority is passed to the player again
    browser.player1().getActionHelper().canContinue();
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
    browser.player2().getActionHelper().cannotContinue();
    browser.player2().getPhaseHelper().is(M1, OPPONENT);
    browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Legion's Judgment"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Huatli's Snubhorn"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Colossal Dreadmaw"));
    }
  }
}
