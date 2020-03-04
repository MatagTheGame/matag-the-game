package application.cleanup;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.MtgGameApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cardinstance.modifiers.TappedModifier.TAPPED;
import static com.aa.mtg.cards.properties.PowerToughness.powerToughness;
import static com.aa.mtg.game.turn.phases.EndTurnPhase.ET;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;
import static com.aa.mtg.game.turn.phases.UpkeepPhase.UP;
import static com.aa.mtg.player.PlayerType.OPPONENT;
import static com.aa.mtg.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CleanupTest.InitGameTestConfiguration.class})
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

    // When Player1 clicks continue
    browser.player1().getActionHelper().clickContinue();

    // Phase is
    browser.player1().getPhaseHelper().is(M2, PLAYER);

    // When Player1 clicks continue
    browser.player1().getActionHelper().clickContinue();

    // Phase is
    browser.player1().getPhaseHelper().is(ET, OPPONENT);
    browser.player2().getPhaseHelper().is(ET, PLAYER);
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(UP, OPPONENT);

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
      huatlisShubhorn.getModifiers().setTapped(TAPPED);
      huatlisShubhorn.getModifiers().dealDamage(1);

      CardInstance grazingWhiptail = gameStatus.getCurrentPlayer().getBattlefield().getCards().get(1);
      grazingWhiptail.getModifiers().setSummoningSickness(true);
      grazingWhiptail.getModifiers().addExtraPowerToughnessUntilEndOfTurn(powerToughness("1/1"));

      // Non Current Player
      addCardToNonCurrentPlayerLibrary(gameStatus, cards.get("Mountain"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Air Elemental"));
      CardInstance nestRobber = gameStatus.getNonCurrentPlayer().getBattlefield().getCards().get(0);
      nestRobber.getModifiers().dealDamage(1);
      nestRobber.getModifiers().setTapped(TAPPED);
    }
  }
}
