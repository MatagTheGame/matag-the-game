package application.cleanup;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.cards.properties.PowerToughness.powerToughness;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

public class CleanupTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CleanupTest.InitTestServiceForTest());
  }

  @Test
  public void cleanupWhenPassingTurns() {
    // Battlefields is
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Huatli's Snubhorn")).hasDamage(1);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Huatli's Snubhorn")).isTapped();

    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).hasSummoningSickness();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("4/5");

    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).hasDamage(1);
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).isTapped();

    // Phase is
    browser.player1().getPhaseHelper().is(M1, PLAYER);

    // Player1 goes to M2
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);

    // Player1 passes the turn
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);

    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Huatli's Snubhorn")).doesNotHaveDamage();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Huatli's Snubhorn")).isTapped();

    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).hasSummoningSickness();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("3/4");

    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).isNotTapped();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Air Elemental")).doesNotHaveDamage();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      // Current Player
      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Island"));

      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Huatli's Snubhorn"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Grazing Whiptail"));

      CardInstance huatlisShubhorn = gameStatus.getCurrentPlayer().getBattlefield().getCards().get(0);
      huatlisShubhorn.getModifiers().setTapped(true);
      huatlisShubhorn.getModifiers().dealDamage(1);

      CardInstance grazingWhiptail = gameStatus.getCurrentPlayer().getBattlefield().getCards().get(1);
      grazingWhiptail.getModifiers().setSummoningSickness(true);
      grazingWhiptail.getModifiers().getModifiersUntilEndOfTurn().addExtraPowerToughnessUntilEndOfTurn(powerToughness("1/1"));

      // Non Current Player
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Air Elemental"));
      CardInstance airElemental = gameStatus.getNonCurrentPlayer().getBattlefield().getCards().get(0);
      airElemental.getModifiers().dealDamage(1);
      airElemental.getModifiers().setTapped(true);
    }
  }
}
