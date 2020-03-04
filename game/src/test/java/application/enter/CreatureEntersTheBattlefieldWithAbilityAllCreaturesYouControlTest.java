package application.enter;

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

import static application.browser.BattlefieldHelper.*;
import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureEntersTheBattlefieldWithAbilityAllCreaturesYouControlTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CreatureEntersTheBattlefieldWithAbilityAllCreaturesYouControlTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldWithAbilityAllCreaturesYouControlTest.InitTestServiceForTest());
  }

  @Test
  public void creatureEntersTheBattlefieldWithAbilityAllCreaturesYouControl() {
    // When Playing Angel of the Dawn
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 4).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Angel of the Dawn")).click();
    browser.player2().getActionHelper().clickContinue();
    int angelOfTheDawnId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Angel of the Dawn")).getCardIdNumeric();

    // Then the enter the battlefield event gets triggered
    browser.player1().getStackHelper().containsAbility("Pippo's Angel of the Dawn (" + angelOfTheDawnId + "): Creatures you control get +1/+1 and vigilance until end of turn.");
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();

    // Which increases other creatures
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("4/5");

    // And gives them vigilance
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getPhaseHelper().is(BC, PLAYER);
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Enforcer Griffin")).declareAsAttacker();
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Enforcer Griffin")).isNotTapped();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Angel of the Dawn"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Enforcer Griffin"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    }
  }
}
