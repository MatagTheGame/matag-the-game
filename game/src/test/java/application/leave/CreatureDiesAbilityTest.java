package application.leave;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.aa.mtg.game.MtgApplication;
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
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.CoreSet2019.MURDER;
import static com.aa.mtg.cards.sets.WarOfTheSpark.GOBLIN_ASSAULT_TEAM;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static java.util.Collections.singletonList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureDiesAbilityTest.InitGameTestConfiguration.class})
public class CreatureDiesAbilityTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CreatureDiesAbilityTest.InitTestServiceForTest());
    }

    @Test
    public void creatureDiesAbility() {
        int firstGoblinId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(GOBLIN_ASSAULT_TEAM, 0).getCardIdNumeric();
        int secondGoblinId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(GOBLIN_ASSAULT_TEAM, 1).getCardIdNumeric();

        // When opponent kills 1 goblin
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 0).tap();
        browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 1).tap();
        browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 2).tap();
        browser.player2().getHandHelper(PLAYER).getFirstCard(MURDER).click();
        browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(GOBLIN_ASSAULT_TEAM).click();
        browser.player1().getActionHelper().clickContinue();

        // Then put +1/+1 counter is triggered
        browser.player1().getStackHelper().containsAbilitiesExactly(singletonList("Pippo's Goblin Assault Team (" + firstGoblinId + "): That targets get 1 +1/+1 counters."));

        // When clicking on the other goblin
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GOBLIN_ASSAULT_TEAM).target();
        browser.player2().getActionHelper().clickContinue();

        // Then that goblin gets a counter
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GOBLIN_ASSAULT_TEAM).hasPlus1Counters(1);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GOBLIN_ASSAULT_TEAM).hasPowerAndToughness("5/2");
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, GOBLIN_ASSAULT_TEAM);
            addCardToCurrentPlayerBattlefield(gameStatus, GOBLIN_ASSAULT_TEAM);

            addCardToNonCurrentPlayerHand(gameStatus, MURDER);
            addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
            addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
            addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
        }
    }
}
