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
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.sets.CoreSet2019.BOGSTOMPER;
import static com.aa.mtg.cards.sets.CoreSet2019.EXCLUSION_MAGE;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CreatureEntersTheBattlefieldReturnTargetToHandTest.InitGameTestConfiguration.class})
public class CreatureEntersTheBattlefieldReturnTargetToHandTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CreatureEntersTheBattlefieldReturnTargetToHandTest.InitTestServiceForTest());
    }

    @Test
    public void creatureEntersTheBattlefieldReturnTargetToHand() {
        // When Exclusion Mage
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(EXCLUSION_MAGE).click();
        browser.player2().getActionHelper().clickContinue();

        // Then Exclusion Mage is on the battlefield and its trigger on the stack
        int exclusionMageId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(EXCLUSION_MAGE).getCardIdNumeric();
        browser.player1().getStackHelper().containsAbility("Pippo's Exclusion Mage (" + exclusionMageId + "): That targets get returned to its owner's hand.");

        // When player 1 selects opponent creature to return
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(BOGSTOMPER).click();
        browser.player2().getActionHelper().clickContinue();

        // Then it's returned to its owner hand
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).isEmpty();
        browser.player1().getHandHelper(OPPONENT).toHaveSize(1);

        // When playing another Exclusion Mage
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 3).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 4).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 5).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(EXCLUSION_MAGE).click();
        browser.player2().getActionHelper().clickContinue();

        // Player 1 continue without targets as nothing can be targeted
        browser.player1().getActionHelper().clickContinue();
        browser.player1().getStackHelper().isEmpty();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(EXCLUSION_MAGE, EXCLUSION_MAGE);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, EXCLUSION_MAGE);
            addCardToCurrentPlayerHand(gameStatus, EXCLUSION_MAGE);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);

            addCardToNonCurrentPlayerBattlefield(gameStatus, BOGSTOMPER);
        }
    }
}
