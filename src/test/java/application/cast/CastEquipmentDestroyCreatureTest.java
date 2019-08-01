package application.cast;

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
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.Dominaria.SHORT_SWORD;
import static com.aa.mtg.cards.sets.Ixalan.LEGIONS_JUDGMENT;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.PROWLING_CARACAL;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastEquipmentDestroyCreatureTest.InitGameTestConfiguration.class})
public class CastEquipmentDestroyCreatureTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CastEquipmentDestroyCreatureTest.InitTestServiceForTest());
    }

    @Test
    public void castEquipmentDestroyCreature() {
        // When cast an artifact equipment
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(SHORT_SWORD).click();

        // Equipment goes on the stack
        browser.player1().getStackHelper().containsExactly(SHORT_SWORD);

        // When opponent accepts equipment
        browser.player2().getActionHelper().clickContinue();

        // Then the attachment and its effect are on the battlefield
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(SHORT_SWORD);
        int shortSwordId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(SHORT_SWORD).getCardIdNumeric();

        // When equipping
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 2).tap();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(SHORT_SWORD).select();
        browser.player1().getStatusHelper().hasMessage("Select targets for Short Sword.");
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(PROWLING_CARACAL).click();

        // Equip ability goes on the stack
        browser.player1().getStackHelper().containsAbility("Pippo's Short Sword (" + shortSwordId + "): Equipped creature gets +1/+1.");

        // When opponent accepts the equip
        browser.player2().getActionHelper().clickContinue();

        // Then the target creature is equipped
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(PROWLING_CARACAL).hasPowerAndToughness("4/2");

        // Destroy the creature
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 3).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 4).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 5).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).select();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(PROWLING_CARACAL).click();
        browser.player2().getActionHelper().clickContinue();

        // Creature is in the graveyard
        browser.player1().getGraveyardHelper(PLAYER).contains(PROWLING_CARACAL, LEGIONS_JUDGMENT);

        // Creature is still in the battlefield
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(SHORT_SWORD);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, PROWLING_CARACAL);
            addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);
            addCardToCurrentPlayerHand(gameStatus, SHORT_SWORD);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        }
    }
}
