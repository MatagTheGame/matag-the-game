package application.enter;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.matag.game.turn.phases.main1.Main1Phase.M1;
import static com.matag.game.turn.phases.main2.Main2Phase.M2;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.matag.cards.Cards;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;

public class WheneverACreatureEntersTheBattlefieldAbilityTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new WheneverACreatureEntersTheBattlefieldAbilityTest.InitTestServiceForTest());
  }

  @Test
  public void wheneverACreatureEntersTheBattlefieldAbility() {
    // When Playing Ajani's Welcome
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 0).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Ajani's Welcome")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Ajani's Welcome"));

    // And then a creature
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Daybreak Chaplain")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then a the creature is on the battlefield and event is triggered
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Daybreak Chaplain"));
    int ajanisWelcomeId1 = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Ajani's Welcome"), 0).getCardIdNumeric();
    browser.player1().getStackHelper().containsAbility("Player1's Ajani's Welcome (" + ajanisWelcomeId1 + "): You gain 1 life.");

    // When players continue
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then player 1 gains 1 life
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(21);

    // When Playing another Ajani's Welcome
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Ajani's Welcome")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Ajani's Welcome"));

    // And then another creature
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 4).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 5).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Daybreak Chaplain")).click();
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then a both creatures are on the battlefield two events is triggered
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Daybreak Chaplain"), cards.get("Daybreak Chaplain"));
    int ajanisWelcomeId2 = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Ajani's Welcome"), 1).getCardIdNumeric();
    browser.player1().getStackHelper().containsAbilitiesExactly(asList(
      "Player1's Ajani's Welcome (" + ajanisWelcomeId1 + "): You gain 1 life.",
      "Player1's Ajani's Welcome (" + ajanisWelcomeId2 + "): You gain 1 life."
    ));

    // When players continue
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M1, PLAYER);

    // Then player 1 gains 1 life
    browser.player1().getStackHelper().containsAbility("Player1's Ajani's Welcome (" + ajanisWelcomeId1 + "): You gain 1 life.");
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(22);

    // When players continue
    browser.player1().getActionHelper().clickContinueAndExpectPhase(M1, OPPONENT);
    browser.player2().getActionHelper().clickContinueAndExpectPhase(M2, PLAYER);

    // Then player 1 gains 1 life
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(23);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Ajani's Welcome"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Ajani's Welcome"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Daybreak Chaplain"));
      addCardToCurrentPlayerHand(gameStatus, cards.get("Daybreak Chaplain"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"));
    }
  }
}
