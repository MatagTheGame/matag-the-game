package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.MatagGameApplication;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.COMBAT_LINE;
import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static com.matag.game.turn.phases.BeginCombatPhase.BC;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.Main2Phase.M2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CombatTrampleHasteTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CombatTrampleHasteTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CombatTrampleHasteTest.InitTestServiceForTest());
  }

  @Test
  public void combatTrampleHaste() {
    // Playing card with trample haste
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 3).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Mountain"), 4).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Charging Monstrosaur")).click();
    browser.player2().getActionHelper().clickContinue();

    // When going to combat
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getPhaseHelper().is(BC, PLAYER);
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, PLAYER);

    // When declare attacker
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Charging Monstrosaur")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinue();

    // Declare blocker
    browser.player2().getPhaseHelper().is(DA, PLAYER);
    browser.player2().getActionHelper().clickContinue();
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Charging Monstrosaur")).select();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Huatli's Snubhorn")).declareAsBlocker();
    browser.player2().getActionHelper().clickContinue();

    // No instant played during combat
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();

    // Then
    browser.player1().getPhaseHelper().is(M2, PLAYER);
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(17);

    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Huatli's Snubhorn"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Charging Monstrosaur")).hasDamage(2);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Mountain"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Charging Monstrosaur"));
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Huatli's Snubhorn"));
    }
  }
}
