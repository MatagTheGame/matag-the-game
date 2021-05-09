package application.cast;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;

public class PlayLandTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new PlayLandTest.InitTestServiceForTest());
  }

  @Test
  public void playLand() {
    // When play first land
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Island")).click();

    // Then battlefields contain land
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).containsExactly(cards.get("Island"));
    browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).containsExactly(cards.get("Island"));

    // Hand is empty
    browser.player1().getHandHelper(PLAYER).toHaveSize(1);
    browser.player2().getHandHelper(OPPONENT).toHaveSize(1);

    // When play second land
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Island")).click();

    // Then error is displayed
    browser.player1().getMessageHelper().hasMessage("You already played a land this turn.");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Island"));
    }
  }
}
