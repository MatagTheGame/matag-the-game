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

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({WheneverACreatureEntersTheBattlefieldAbilityTest.InitGameTestConfiguration.class})
@Category(Regression.class)
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
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Ajani's Welcome"));

    // And then a creature
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Daybreak Chaplain")).click();
    browser.player2().getActionHelper().clickContinue();

    // Then a the creature is on the battlefield and event is triggered
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Daybreak Chaplain"));
    int ajanisWelcomeId1 = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Ajani's Welcome"), 0).getCardIdNumeric();
    browser.player1().getStackHelper().containsAbility("Pippo's Ajani's Welcome (" + ajanisWelcomeId1 + "): Gain 1 life.");

    // When players continue
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();

    // Then player 1 gains 1 life
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(21);

    // When Playing another Ajani's Welcome
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 3).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Ajani's Welcome")).click();
    browser.player2().getActionHelper().clickContinue();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Ajani's Welcome"));

    // And then another creature
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 4).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Plains"), 5).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Daybreak Chaplain")).click();
    browser.player2().getActionHelper().clickContinue();

    // Then a both creatures are on the battlefield two events is triggered
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Daybreak Chaplain"), cards.get("Daybreak Chaplain"));
    int ajanisWelcomeId2 = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Ajani's Welcome"), 1).getCardIdNumeric();
    browser.player1().getStackHelper().containsAbilitiesExactly(asList(
      "Pippo's Ajani's Welcome (" + ajanisWelcomeId1 + "): Gain 1 life.",
      "Pippo's Ajani's Welcome (" + ajanisWelcomeId2 + "): Gain 1 life."
    ));

    // When players continue
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();

    // Then player 1 gains 1 life
    browser.player1().getStackHelper().containsAbility("Pippo's Ajani's Welcome (" + ajanisWelcomeId1 + "): Gain 1 life.");
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(22);

    // When players continue
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();

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
