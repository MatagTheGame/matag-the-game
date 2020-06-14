package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static application.browser.BattlefieldHelper.COMBAT_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.DeclareAttackersPhase.DA;
import static com.matag.game.turn.phases.DeclareBlockersPhase.DB;
import static com.matag.game.turn.phases.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;

public class BasicCombatTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new BasicCombatTest.InitTestServiceForTest());
  }

  @Test
  public void basicCombat() {
    // When continuing
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DA, PLAYER);

    // Message and status are about declaring attackers
    browser.player1().getStatusHelper().hasMessage("Choose creatures you want to attack with.");

    // When declare attacker
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Headwater Sentries")).click();

    // Then attacker is moved forward
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Headwater Sentries")).isFrontendTapped();

    // When withdraw attacker
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Headwater Sentries")).click();

    // Then attacker is moved backward
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Headwater Sentries")).isNotTapped();

    // When declare illegal attacker
    int nestRobberId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Nest Robber")).getCardIdNumeric();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Nest Robber")).click();

    // Then a message is displayed
    browser.getMessageHelper().hasMessage("\"" + nestRobberId + " - Nest Robber\" is already tapped and cannot attack.");
    browser.getMessageHelper().close();

    // The four attackers are declared as attacker and continue
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Headwater Sentries")).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Coral Commando")).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Axebane Beast")).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Ancient Brontodon")).click();
    browser.player1().getActionHelper().clickContinueAndExpectPhase(DB, OPPONENT);

    // The phase move to Declare blocker
    browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).toHaveSize(4);
    browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Headwater Sentries")).isTapped();

    // And the opponent sees the same
    browser.player2().getStatusHelper().hasMessage("Choose creatures you want to block with.");
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).toHaveSize(4);
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Headwater Sentries")).isTapped();

    // Opponent select a creature to block
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Ancient Brontodon")).click();
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Ancient Brontodon")).isSelected();

    // Opponent decide to block that creature
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Coral Commando")).click();
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).contains(cards.get("Coral Commando"));

    // Opponent decide to withdraw that block
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Coral Commando")).click();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Coral Commando"));

    // Opponent declare blockers
    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Headwater Sentries")).click();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Headwater Sentries")).click();
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Headwater Sentries")).parentHasStyle("margin-left: 390px; margin-top: 0px;");

    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Coral Commando")).click();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Coral Commando")).click();
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Coral Commando")).parentHasStyle("margin-left: 130px; margin-top: 0px;");

    browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(cards.get("Axebane Beast")).click();
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Nest Robber")).click();
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(cards.get("Nest Robber")).parentHasStyle("margin-left: -130px; margin-top: 0px;");
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Headwater Sentries")).click();
    browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(cards.get("Headwater Sentries"), 1).parentHasStyle("margin-left: -105px; margin-top: 50px;");

    // Continue to M2
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);

    // Cards are now
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Coral Commando"), cards.get("Axebane Beast"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Headwater Sentries"), cards.get("Ancient Brontodon"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Headwater Sentries")).hasDamage(2);
    browser.player1().getGraveyardHelper(OPPONENT).contains(cards.get("Coral Commando"), cards.get("Nest Robber"));
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(cards.get("Headwater Sentries"), cards.get("Headwater Sentries"));
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getCard(cards.get("Headwater Sentries"), 0).hasDamage(2);
    browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getCard(cards.get("Headwater Sentries"), 1).hasDamage(2);

    browser.player2().getGraveyardHelper(OPPONENT).contains(cards.get("Coral Commando"), cards.get("Axebane Beast"));
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(cards.get("Headwater Sentries"), cards.get("Ancient Brontodon"));
    browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(cards.get("Headwater Sentries")).hasDamage(2);
    browser.player2().getGraveyardHelper(PLAYER).contains(cards.get("Coral Commando"), cards.get("Nest Robber"));
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Headwater Sentries"), cards.get("Headwater Sentries"));
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Headwater Sentries"), 0).hasDamage(2);
    browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Headwater Sentries"), 1).hasDamage(2);

    // Life is
    browser.player2().getPlayerInfoHelper(PLAYER).toHaveLife(11);
    browser.player2().getPlayerInfoHelper(OPPONENT).toHaveLife(20);

    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(20);
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(11);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      // Single block both survive
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Headwater Sentries")); // 2/5
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Headwater Sentries")); // 2/5

      // Single block both die
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Coral Commando")); // 3/2
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Coral Commando")); // 3/2

      // Double block attacker dies one blocker dies
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Axebane Beast")); // 3/4
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Nest Robber")); // 2/1
      addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Headwater Sentries")); // 2/5

      // Non blocked damage to player
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Ancient Brontodon")); // 9/9

      // Cannot attack as tapped
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Nest Robber"));
      gameStatus.getCurrentPlayer().getBattlefield().getCards().get(4).getModifiers().setTapped(true);
    }
  }
}
