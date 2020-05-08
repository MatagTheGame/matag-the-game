package application.ability;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.phases.Main1Phase;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.*;
import static com.matag.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.player.PlayerType.PLAYER;

@Category(Regression.class)
public class ActivatedAbilityOnCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new ActivatedAbilityOnCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void activatedAbilityOnCreature() {
    // Playing jousting dummy
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Jousting Dummy")).click();
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(Main1Phase.M1, PLAYER);

    // When increasing jousting dummy (as well on summoning sickness creature)
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 4).tap();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Jousting Dummy"), 1).click();

    int secondJoustingDummyId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Jousting Dummy"), 1).getCardIdNumeric();

    // then ability goes on the stack
    browser.player1().getStackHelper().containsAbility("Player1's Jousting Dummy (" + secondJoustingDummyId + "): Gets +1/+0 until end of turn.");

    // opponent accepts the ability
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(Main1Phase.M1, PLAYER);

    // power of jousting dummy is increased
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Jousting Dummy"), 1).hasSummoningSickness();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Jousting Dummy"), 1).hasPowerAndToughness("3/1");

    // move at AfterBlocking phase
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Jousting Dummy")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(AB, PLAYER);

    // check can increase jousting dummy at instant speed
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 5).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 6).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 7).tap();
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Jousting Dummy"), 0).click();

    int firstJoustingDummyId = browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Jousting Dummy"), 0).getCardIdNumeric();

    // then ability goes on the stack
    browser.player1().getStackHelper().containsAbility("Player1's Jousting Dummy (" + firstJoustingDummyId + "): Gets +1/+0 until end of turn.");

    // opponent accepts the ability
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(AB, PLAYER);

    // power of jousting dummy is increased
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Jousting Dummy"), 0).doesNotHaveSummoningSickness();
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Jousting Dummy"), 0).hasPowerAndToughness("3/1");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Jousting Dummy"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Jousting Dummy"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    }
  }
}
