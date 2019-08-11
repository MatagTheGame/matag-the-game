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
import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.sets.Ixalan.RAGING_SWORDTOOTH;
import static com.aa.mtg.cards.sets.WarOfTheSpark.ENFORCER_GRIFFIN;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureEntersTheBattlefieldWithAbilityAllOtherCreaturesTest.InitGameTestConfiguration.class})
public class CreatureEntersTheBattlefieldWithAbilityAllOtherCreaturesTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldWithAbilityAllOtherCreaturesTest.InitTestServiceForTest());
    }

    @Test
    public void creatureEntersTheBattlefieldWithAbilityAllOtherCreatures() {
        // When Playing Angel of the Dawn
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(FOREST, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(FOREST, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(FOREST, 2).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(RAGING_SWORDTOOTH).click();
        browser.player2().getActionHelper().clickContinue();
        int ragingSwordtoothId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(RAGING_SWORDTOOTH).getCardIdNumeric();

        // Then the enter the battlefield event gets triggered
        browser.player1().getStackHelper().containsAbility("Pippo's Raging Swordtooth (" + ragingSwordtoothId + "): Other creatures get 1 damage until end of turn.");
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();

        // Which gives 1 damage to other creatures
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(RAGING_SWORDTOOTH).doesNotHaveDamage();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(ENFORCER_GRIFFIN).hasDamage(1);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(ENFORCER_GRIFFIN).hasDamage(1);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, RAGING_SWORDTOOTH);
            addCardToCurrentPlayerBattlefield(gameStatus, ENFORCER_GRIFFIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, FOREST);
            addCardToCurrentPlayerBattlefield(gameStatus, FOREST);
            addCardToCurrentPlayerBattlefield(gameStatus, FOREST);

            addCardToNonCurrentPlayerBattlefield(gameStatus, ENFORCER_GRIFFIN);
        }
    }
}
