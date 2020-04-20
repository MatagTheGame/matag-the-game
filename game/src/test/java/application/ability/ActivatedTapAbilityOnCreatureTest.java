package application.ability;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.MatagGameApplication;
import com.matag.game.cardinstance.CardInstanceSearch;
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
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MatagGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({ActivatedTapAbilityOnCreatureTest.InitGameTestConfiguration.class})
public class ActivatedTapAbilityOnCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new ActivatedTapAbilityOnCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void activatedAbilityOnCreature() {
    // Playing jousting dummy
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Checkpoint Officer")).click();
    browser.player2().getActionHelper().clickContinue();

    // Prepare the mana
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();

    // Playing a tapping ability on a tapped creature does nothing
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Checkpoint Officer"), 0).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Checkpoint Officer"), 0).isNotSelected();

    // Playing a tapping ability on a creature with summoning sickness does nothing
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Checkpoint Officer"), 2).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Checkpoint Officer"), 2).isNotSelected();

    // Playing a tapping ability on a creature with summoning sickness does nothing
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Checkpoint Officer"), 1).select();
    int checkpointOfficerId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Checkpoint Officer"), 1).getCardIdNumeric();
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Jousting Dummy")).target();

    // Ability goes on the stack
    browser.player2().getStackHelper().containsAbility("Player1's Checkpoint Officer (" + checkpointOfficerId + "): That targets get tapped.");
    browser.player2().getActionHelper().clickContinue();

    // Both creatures are tapped
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Jousting Dummy")).isTapped();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Checkpoint Officer"), 1).isTapped();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Checkpoint Officer"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Checkpoint Officer"));
      new CardInstanceSearch(gameStatus.getPlayer1().getBattlefield().getCards()).withName("Checkpoint Officer")
        .getCards().get(0).getModifiers().tap();
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Checkpoint Officer"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));

      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Jousting Dummy"));
    }
  }
}
