package application.ability;

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
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.ThroneOfEldraine.JOUSTING_DUMMY;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({ActivatedAbilityOnCreatureTest.InitGameTestConfiguration.class})
public class ActivatedAbilityOnCreatureTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new ActivatedAbilityOnCreatureTest.InitTestServiceForTest());
    }

    @Test
    public void activatedAbilityOnCreature() {
        // When increasing jousting dummy
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 2).tap();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(JOUSTING_DUMMY).click();

        int joustingDummyId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(JOUSTING_DUMMY).getCardIdNumeric();

        // then ability goes on the stack
        browser.player1().getStackHelper().containsAbility("Pippo's Jousting Dummy (" + joustingDummyId + "): Gets +1/+0 until end of turn.");

        // opponent accepts the ability
        browser.player2().getActionHelper().clickContinue();

        // power of jousting dummy is increased
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(JOUSTING_DUMMY).hasPowerAndToughness("3/1");
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, JOUSTING_DUMMY);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        }
    }
}
