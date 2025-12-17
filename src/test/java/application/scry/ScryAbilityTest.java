package application.scry;

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
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Tag("RegressionTest")
public class ScryAbilityTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new ScryAbilityTest.InitTestServiceForTest());
  }

  @Test
  public void scryAbilityTest() {
    // Given Player Plays Get the Point
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Mountain")).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 3).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Get the Point")).select();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Feral Maaka")).click();

    // And Opponent accepts it
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Player can see the top card
    browser.player1().getLibraryHelper(PLAYER).getVisibleCardsHelper().contains("Swamp");
    browser.player1().getLibraryHelper(PLAYER).getVisibleCardsHelper().getFirstCard(cards.get("Swamp")).click();

    // FIXME now clicking on the card should show two options: keep on top or put to bottom.
    System.out.println();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Feral Maaka"));
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));

      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Get the Point"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
    }
  }
}
