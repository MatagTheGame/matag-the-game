package application.enter;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.matag.cards.Cards;
import com.matag.game.MatagGameApplication;
import com.matag.game.init.test.InitTestService;
import com.matag.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
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
@Import({CreatureEntersTheBattlefieldDoubleAbilityTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CreatureEntersTheBattlefieldDoubleAbilityTest extends AbstractApplicationTest {

  @Autowired
  private InitTestServiceDecorator initTestServiceDecorator;

  @Autowired
  private Cards cards;

  public void setupGame() {
    initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldDoubleAbilityTest.InitTestServiceForTest());
  }

  @Test
  public void creatureEntersTheBattlefieldDoubleAbility() {
    // When Playing Dusk Legion Zealot
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 0).tap();
    browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(cards.get("Swamp"), 1).tap();
    browser.player1().getHandHelper(PLAYER).getFirstCard(cards.get("Dusk Legion Zealot")).click();
    browser.player2().getActionHelper().clickContinue();
    int duskLegionZealotId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(cards.get("Dusk Legion Zealot")).getCardIdNumeric();
    browser.player1().getStackHelper().containsAbility("Player1's Dusk Legion Zealot (" + duskLegionZealotId + "): Draw 1 cards. Lose 1 life.");
    browser.player1().getActionHelper().clickContinue();
    browser.player2().getActionHelper().clickContinue();

    // Then both abilities happen
    browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(cards.get("Dusk Legion Zealot"));
    browser.player1().getHandHelper(PLAYER).containsExactly(cards.get("Swamp"));
    browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(19);
  }

  static class InitTestServiceForTest extends InitTestService {
    @Override
    public void initGameStatus(GameStatus gameStatus) {
      addCardToCurrentPlayerHand(gameStatus, cards.get("Dusk Legion Zealot"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));
      addCardToCurrentPlayerLibrary(gameStatus, cards.get("Swamp"));
    }
  }
}
