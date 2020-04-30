package application.cast;

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
import static com.matag.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@Category(Regression.class)
public class CastCreatureWithFlashGivingAbilitiesToOtherCreatures extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastCreatureWithFlashGivingAbilitiesToOtherCreatures.InitTestServiceForTest());
  }

  @Test
  public void testCreatureWithFlashGivingAbilitiesToOtherCreature() {
    // Going to combat
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, PLAYER);

    // Attack with Ardenvale Paladin
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Ardenvale Paladin")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinue();
    browser.getPhaseHelper().is(DA, OPPONENT);
    browser.player2().getActionHelper().clickContinue();

    // Block with Ancient Brontodon
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Ancient Brontodon")).declareAsBlocker();
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(AB, PLAYER);

    // Playing Blacklance Paragon
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Blacklance Paragon")).click();
    browser.player1().getStackHelper().contains(cards.get("Blacklance Paragon"));
    browser.player1().getPhaseHelper().is(AB, OPPONENT);

    // Opponent just accepts it
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(AB, PLAYER);

    // Player select Ardenvale Paladin as target
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Ardenvale Paladin")).click();
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(M2, PLAYER);

    // Ancient Brontodon dies because of the deathtouch and player gets 2 life
    browser.player1().getGraveyardHelper(PLAYER).containsExactly(cards.get("Ardenvale Paladin"));
    browser.player1().getGraveyardHelper(OPPONENT).containsExactly(cards.get("Ancient Brontodon"));
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(22);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Blacklance Paragon"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Ardenvale Paladin"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Ancient Brontodon"));
    }
  }
}
