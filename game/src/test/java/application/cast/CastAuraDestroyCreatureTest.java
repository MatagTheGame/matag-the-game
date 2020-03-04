package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
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

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;
import static com.aa.mtg.player.PlayerType.OPPONENT;
import static com.aa.mtg.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastAuraDestroyCreatureTest.InitGameTestConfiguration.class})
public class CastAuraDestroyCreatureTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastAuraDestroyCreatureTest.InitTestServiceForTest());
  }

  @Test
  public void castAuraDestroyCreature() {
    // When cast an enchantment aura
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Candlelight Vigil")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Candlelight Vigil.");
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).target();

    // Enchantment goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Candlelight Vigil"));

    // When opponent accepts enchantment
    browser.player2().getActionHelper().clickContinue();

    // Then the attachment and its effect are on the battlefield
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Candlelight Vigil"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).hasPowerAndToughness("4/5");

    // When casting another instance of the same aura
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 4).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 5).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 6).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 7).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Candlelight Vigil")).select();
    browser.player1().getStatusHelper().hasMessage("Select targets for Candlelight Vigil.");
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).target();

    // Enchantment goes on goes on the stack
    browser.player1().getStackHelper().containsExactly(cards.get("Candlelight Vigil"));

    // When opponent accepts enchantment
    browser.player2().getActionHelper().clickContinue();

    // Then the attachment and its effect are on the battlefield
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Candlelight Vigil"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).hasPowerAndToughness("7/7");

    // Verify as well the vigilance effect
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getPhaseHelper().is(BC, PLAYER);
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).declareAsAttacker();
    browser.player1().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(DA, OPPONENT);
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getPhaseHelper().is(M2, PLAYER);
    browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(13);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).isNotTapped();

    // Destroy the creature
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 8).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 9).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 10).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 11).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Legion's Judgment")).select();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Concordia Pegasus")).target();
    browser.player2().getActionHelper().clickContinue();

    // Creature and its enchantments are in the graveyard
    browser.player1().getGraveyardHelper(PLAYER).contains(cards.get("Concordia Pegasus"), cards.get("Candlelight Vigil"), cards.get("Candlelight Vigil"), cards.get("Legion's Judgment"));
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).isEmpty();
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Concordia Pegasus"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Legion's Judgment"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Candlelight Vigil"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Candlelight Vigil"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
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
