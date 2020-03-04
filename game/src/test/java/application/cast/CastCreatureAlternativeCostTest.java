package application.cast;

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
import static com.aa.mtg.cards.properties.Color.BLUE;
import static com.aa.mtg.cards.properties.Color.GREEN;
import static com.aa.mtg.cards.properties.Color.WHITE;
import static com.aa.mtg.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgGameApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastCreatureAlternativeCostTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CastCreatureAlternativeCostTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CastCreatureAlternativeCostTest.InitTestServiceForTest());
  }

  @Test
  public void castCreatureAlternativeCost() {
    // When click on creature without paying the cost
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Headwater Sentries")).click();

    // Then stack is still empty
    browser.player1().getStackHelper().toHaveSize(0);

    // When clicking the dual land
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Azorius Guildgate")).click();

    // Then it's possible to chose which mana generate
    browser.player1().getPlayableAbilitiesHelper().toHaveAbilities(asList("TAP: add WHITE", "TAP: add BLUE"));

    // When clicking on the WHITE
    browser.player1().getPlayableAbilitiesHelper().playAbility(0);

    // Then WHITE mana is generated
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Azorius Guildgate")).isFrontendTapped();
    browser.player1().getPlayerActiveManaHelper().toHaveMana(singletonList(WHITE));

    // When clicking on other lands and try to play the creature
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Druid of the Cowl"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Druid of the Cowl"), 1).tap();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Druid of the Cowl"), 2).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Headwater Sentries")).click();

    // Then the creature is not played
    browser.player1().getHandHelper(PLAYER).contains(cards.get("Headwater Sentries"));

    // When clicking the dual land
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Azorius Guildgate")).click();

    // The dual land gets untapped
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Azorius Guildgate")).isNotFrontendTapped();
    browser.player1().getPlayerActiveManaHelper().toHaveMana(asList(GREEN, GREEN, GREEN));

    // Untapping 2 creatures
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Druid of the Cowl"), 1).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Druid of the Cowl"), 1).isNotFrontendTapped();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Druid of the Cowl"), 2).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(cards.get("Druid of the Cowl"), 2).isNotFrontendTapped();

    // When clicking the dual land for BLUE
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Azorius Guildgate")).click();
    browser.player1().getPlayableAbilitiesHelper().playAbility(1);

    // Then BLUE mana is generated
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getFirstCard(cards.get("Azorius Guildgate")).isFrontendTapped();
    browser.player1().getPlayerActiveManaHelper().toHaveMana(asList(BLUE, GREEN));

    // When cards.get("Gyre Engineer") is clicked
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Gyre Engineer")).click();
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Gyre Engineer")).isFrontendTapped();

    // Then 2 mana are generated
    browser.player1().getPlayerActiveManaHelper().toHaveMana(asList(BLUE, BLUE, GREEN, GREEN));

    // When clicking on the creature
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Headwater Sentries")).click();

    // creature is now played
    browser.player1().getStackHelper().contains(cards.get("Headwater Sentries"));
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Headwater Sentries"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Azorius Guildgate"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Druid of the Cowl"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Druid of the Cowl"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Druid of the Cowl"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Gyre Engineer"));
    }
  }
}
