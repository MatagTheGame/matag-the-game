package application.enter;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.*;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

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
    browser.player1().getPhaseHelper().is("M1", PLAYER);
    int angelOfTheDawnId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Angel of the Dawn")).getCardIdNumeric();

    // Then the enter the battlefield event gets triggered
    browser.player1().getStackHelper().containsAbility("Player1's Angel of the Dawn (" + angelOfTheDawnId + "): Creatures you control get +1/+1 and vigilance until end of turn.");
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is("M1", OPPONENT);
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is("M1", PLAYER);

    // Which increases other creatures
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("4/5");

    // And gives them vigilance
    browser.player1().getActionHelper().clickContinue();
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
