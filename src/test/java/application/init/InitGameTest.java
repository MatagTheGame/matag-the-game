package application.init;

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

public class InitGameTest extends AbstractApplicationTest {
  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new InitTestServiceForTest());
  }

  @Test
  public void display() {
    // Hands are
    browser.player1().getHandHelper(PLAYER).containsExactly(cards.get("Island"), cards.get("Legion's Judgment"));
    browser.player1().getHandHelper(OPPONENT).containsExactly("card", "card");
    browser.player2().getHandHelper(PLAYER).containsExactly(cards.get("Forest"), cards.get("Charging Monstrosaur"));
    browser.player2().getHandHelper(OPPONENT).containsExactly("card", "card");

    // Battlefields are
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).containsExactly(cards.get("Plains"), cards.get("Plains"));
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).containsExactly(cards.get("Grazing Whiptail"));
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("3/4");
    browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).containsExactly(cards.get("Plains"), cards.get("Plains"));
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).containsExactly(cards.get("Grazing Whiptail"));
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("3/4");

    // Graveyards are
    browser.player1().getGraveyardHelper(PLAYER).containsExactly(cards.get("Plains"));
    browser.player1().getGraveyardHelper(OPPONENT).containsExactly(cards.get("Mountain"));
    browser.player2().getGraveyardHelper(PLAYER).containsExactly(cards.get("Mountain"));
    browser.player2().getGraveyardHelper(OPPONENT).containsExactly(cards.get("Plains"));

    // PlayerInfos are
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveName("Player1");
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(20);
    browser.player1().getPlayerInfoHelper(PLAYER).toBeActive();
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveName("Player2");
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(20);
    browser.player1().getPlayerInfoHelper(OPPONENT).toBeInactive();
    browser.player2().getPlayerInfoHelper(PLAYER).toHaveName("Player2");
    browser.player2().getPlayerInfoHelper(PLAYER).toHaveLife(20);
    browser.player2().getPlayerInfoHelper(PLAYER).toBeInactive();
    browser.player2().getPlayerInfoHelper(OPPONENT).toHaveName("Player1");
    browser.player2().getPlayerInfoHelper(OPPONENT).toHaveLife(20);
    browser.player2().getPlayerInfoHelper(OPPONENT).toBeActive();

    // Phase and statuses are
    browser.player1().getPhaseHelper().is(M1, PLAYER);
    browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
    browser.player1().getActionHelper().canContinue();
    browser.player2().getPhaseHelper().is(M1, OPPONENT);
    browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    browser.player2().getActionHelper().cannotContinue();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      // Current Player
      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"));

      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Huatli's Snubhorn"));

      addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Legion's Judgment"));

      addCardToCurrentPlayerGraveyard(gameStatus, cards.get("Plains"));

      // Non Current Player
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Forest"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Grazing Whiptail"));

      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Forest"));
      addCardToNonCurrentPlayerHand(gameStatus, cards.get("Charging Monstrosaur"));

      addCardToNonCurrentPlayerGraveyard(gameStatus, cards.get("Mountain"));
    }
  }
}
