package application.enter;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.aa.mtg.MtgApplication;
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
import static com.aa.mtg.cards.sets.RivalsOfIxalan.DUSK_LEGION_ZEALOT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureEntersTheBattlefieldDoubleAbilityTest.InitGameTestConfiguration.class})
public class CreatureEntersTheBattlefieldDoubleAbilityTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldDoubleAbilityTest.InitTestServiceForTest());
    }

    @Test
    public void creatureEntersTheBattlefieldDoubleAbility() {
        // When Playing Dusk Legion Zealot
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 1).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(DUSK_LEGION_ZEALOT).click();
        browser.player2().getActionHelper().clickContinue();
        int duskLegionZealotId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(DUSK_LEGION_ZEALOT).getCardIdNumeric();
        browser.player1().getStackHelper().containsAbility("Pippo's Dusk Legion Zealot (" + duskLegionZealotId + "): Draw 1 cards. Lose 1 life.");
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();

        // Then both abilities happen
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(DUSK_LEGION_ZEALOT);
        browser.player1().getHandHelper(PLAYER).contains(SWAMP);
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(19);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, DUSK_LEGION_ZEALOT);
            addCardToCurrentPlayerBattlefield(gameStatus, SWAMP);
            addCardToCurrentPlayerBattlefield(gameStatus, SWAMP);
            addCardToCurrentPlayerLibrary(gameStatus, SWAMP);
        }
    }
}
