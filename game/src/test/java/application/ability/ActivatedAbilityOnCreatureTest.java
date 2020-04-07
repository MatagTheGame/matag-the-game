package application.ability;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.MatagGameApplication;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({ActivatedAbilityOnCreatureTest.InitGameTestConfiguration.class})
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

    // When increasing jousting dummy
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 4).tap();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Jousting Dummy")).click();

    int joustingDummyId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Jousting Dummy")).getCardIdNumeric();

    // then ability goes on the stack
    browser.player1().getStackHelper().containsAbility("Player1's Jousting Dummy (" + joustingDummyId + "): Gets +1/+0 until end of turn.");

    // opponent accepts the ability
    browser.player2().getActionHelper().clickContinue();

    // power of jousting dummy is increased
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Jousting Dummy")).hasPowerAndToughness("3/1");
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Jousting Dummy"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    }
  }
}
