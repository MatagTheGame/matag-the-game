package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.aa.mtg.MtgApplication;
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
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.Ixalan.COLOSSAL_DREADMAW;
import static com.aa.mtg.cards.sets.Ixalan.HUATLIS_SNUBHORN;
import static com.aa.mtg.cards.sets.Ixalan.LEGIONS_JUDGMENT;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastSorceryDestroyingCreatureTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CastSorceryDestroyingCreatureTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CastSorceryDestroyingCreatureTest.InitTestServiceForTest());
    }

    @Test
    public void castSorceryDestroyingCreature() {
        // When clicking all lands
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).tap();

        // When click on a sorcery that requires target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then card is selected and message ask to choose a target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).isSelected();
        browser.player1().getStatusHelper().hasMessage("Select targets for Legion's Judgment.");

        // When un-selecting the card
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then card is unselected
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).isNotSelected();
        browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");

        // When click on a sorcery that requires target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then again card is selected and message ask to choose a target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).isSelected();
        browser.player1().getStatusHelper().hasMessage("Select targets for Legion's Judgment.");

        // When clicking a wrong target
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).click();

        // The card is still on the hand selected
        browser.player1().getMessageHelper().hasMessage("Selected targets were not valid.");
        browser.player1().getMessageHelper().close();
        browser.player1().getMessageHelper().hasNoMessage();

        // When clicking again on the sorcery and on a valid target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(COLOSSAL_DREADMAW).click();

        // Then spell goes on the stack
        browser.player1().getStackHelper().containsExactly(LEGIONS_JUDGMENT);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(COLOSSAL_DREADMAW).isTargeted();
        browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
        browser.player2().getStackHelper().containsExactly(LEGIONS_JUDGMENT);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(COLOSSAL_DREADMAW).isTargeted();
        browser.player2().getStatusHelper().hasMessage("Play any instant or ability or resolve the top spell in the stack (SPACE).");

        // And priority is passed to the opponent
        browser.player1().getActionHelper().cannotContinue();
        browser.player1().getPhaseHelper().is(M1, OPPONENT);
        browser.player2().getActionHelper().canContinue();
        browser.player2().getPhaseHelper().is(M1, PLAYER);

        // When player 2 resolves the spell
        browser.player2().getActionHelper().clickContinue();

        // Then the creature is destroyed
        browser.player1().getStackHelper().isEmpty();
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).doesNotContain(COLOSSAL_DREADMAW);
        browser.player1().getGraveyardHelper(OPPONENT).contains(COLOSSAL_DREADMAW);
        browser.player1().getGraveyardHelper(PLAYER).contains(LEGIONS_JUDGMENT);
        browser.player2().getStackHelper().isEmpty();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).doesNotContain(COLOSSAL_DREADMAW);
        browser.player2().getGraveyardHelper(PLAYER).contains(COLOSSAL_DREADMAW);
        browser.player2().getGraveyardHelper(OPPONENT).contains(LEGIONS_JUDGMENT);

        // And priority is passed to the player again
        browser.player1().getActionHelper().canContinue();
        browser.player1().getPhaseHelper().is(M1, PLAYER);
        browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
        browser.player2().getActionHelper().cannotContinue();
        browser.player2().getPhaseHelper().is(M1, OPPONENT);
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);

            addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
            addCardToNonCurrentPlayerBattlefield(gameStatus, HUATLIS_SNUBHORN);
            addCardToNonCurrentPlayerBattlefield(gameStatus, COLOSSAL_DREADMAW);
        }
    }
}
