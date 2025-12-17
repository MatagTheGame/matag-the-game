package application.gainControl;

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
public class GainControlCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new GainControlCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void gainControlCreature() {
    // When cast the sorcery
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Act of Treason")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Act of Treason.");
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).click();

    // Sorcery goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Act of Treason"));

    // When opponent accepts
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Player1 now control the creature
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).isEmpty();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).containsExactly(cards.get("Concordia Pegasus"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).isNotTapped();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).doesNotHaveSummoningSickness();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Act of Treason"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"));
      gameStatus.getNonCurrentPlayer().getBattlefield().getCards().get(0).getModifiers().setSummoningSickness(true);
      gameStatus.getNonCurrentPlayer().getBattlefield().getCards().get(0).getModifiers().setTapped(true);
    }
  }
}
