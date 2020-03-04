package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.MtgGameApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.properties.Color.BLUE;
import static com.aa.mtg.cards.properties.Color.RED;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;
import static com.aa.mtg.player.PlayerType.OPPONENT;
import static com.aa.mtg.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastCreatureTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CastCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void castCreature() {
    // When click on creature without paying the cost
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Headwater Sentries")).click();

    // Then stack is still empty
    browser.player1().getStackHelper().toHaveSize(0);

    // When clicking all lands
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 0).click();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 1).click();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 2).click();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).click();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 1).click();

    // Then all lands are front-end tapped
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 0).isFrontendTapped();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 1).isFrontendTapped();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 2).isFrontendTapped();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).isFrontendTapped();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 1).isFrontendTapped();
    browser.player1().getPlayerActiveManaHelper().toHaveMana(asList(BLUE, BLUE, BLUE, RED, RED));

    // When clicking on a land again then gets untapped and when clicking on it again then gets tapped again
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).click();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).isNotFrontendTapped();
    browser.player1().getPlayerActiveManaHelper().toHaveMana(asList(BLUE, BLUE, BLUE, RED));
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).click();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).isFrontendTapped();
    browser.player1().getPlayerActiveManaHelper().toHaveMana(asList(BLUE, BLUE, BLUE, RED, RED));

    // When click on creature
    browser.player1().getHandHelper(PLAYER).getCard(cards.get("Headwater Sentries"), 0).click();

    // Then all lands are tapped for both players
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 0).isTapped();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 1).isTapped();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Island"), 2).isTapped();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).isTapped();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 1).isTapped();

    browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(cards.get("Island"), 0).isTapped();
    browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(cards.get("Island"), 1).isTapped();
    browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(cards.get("Island"), 2).isTapped();
    browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(cards.get("Mountain"), 0).isTapped();
    browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(cards.get("Mountain"), 1).isTapped();

    // And creature is on the stack for both players (hands is empty)
    browser.player1().getStackHelper().containsExactly(cards.get("Headwater Sentries"));
    browser.player1().getHandHelper(PLAYER).isEmpty();
    browser.player2().getStackHelper().containsExactly(cards.get("Headwater Sentries"));
    browser.player1().getHandHelper(OPPONENT).isEmpty();

    // And priority is to opponent with corresponding status
    browser.player1().getPhaseHelper().is(M1, OPPONENT);
    browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    browser.player1().getActionHelper().cannotContinue();
    browser.player2().getPhaseHelper().is(M1, PLAYER);
    browser.player2().getStatusHelper().hasMessage("Play any instant or ability or resolve the top spell in the stack (SPACE).");
    browser.player2().getActionHelper().canContinue();

    // When player2 acknowledge the spell casted
    browser.player2().getActionHelper().clickContinue();

    // Creature goes on the battlefield (stack is empty)
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Headwater Sentries"));
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(cards.get("Headwater Sentries"));
    browser.player1().getStackHelper().isEmpty();
    browser.player2().getStackHelper().isEmpty();

    // And priority is to player again
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
    browser.player1().getActionHelper().canContinue();
    browser.player2().getPhaseHelper().is(M1, OPPONENT);
    browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    browser.player2().getActionHelper().cannotContinue();

    // Hand is now empty
    browser.player1().getHandHelper(PLAYER).isEmpty();
    browser.player2().getHandHelper(PLAYER).isEmpty();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Headwater Sentries"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
    }
  }
}
