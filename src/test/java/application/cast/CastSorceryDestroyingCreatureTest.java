package application.cast;

import application.AbstractApplicationTest;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.status.GameStatusUpdaterService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastSorceryDestroyingCreatureTest.InitGameTestConfiguration.class})
public class CastSorceryDestroyingCreatureTest extends AbstractApplicationTest {
    @Test
    public void castSorceryDestroyingCreature() {
        // When clicking all lands
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).click();

        // When click on a sorcery that requires target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then card is selected and message ask to choose a target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).hasClass("selected");
        browser.player1().getStatusHelper().hasMessage("Select targets for Legion's Judgment.");

        // When un-selecting the card
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then card is unselected
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).doesNotHaveClass("selected");
        browser.player1().getStatusHelper().hasMessage("Play any spell or abilities or continue (SPACE).");

        // When click on a sorcery that requires target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then again card is selected and message ask to choose a target
        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).hasClass("selected");
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

        // TODO Antonio: Then spell goes on the stack
//        browser.player1().getStackHelper().containsAbilitiesExactly(singletonList("Pippo's Legion's Judgment (" + legionsJudgmentId + "): Destroy target."));
//        browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
//        browser.player2().getStackHelper().containsAbilitiesExactly(singletonList("Pippo's Legion's Judgment (" + legionsJudgmentId + "): Destroy target."));
//        browser.player2().getStatusHelper().hasMessage("Play any instant or abilities or resolve the top spell in the stack (SPACE).");

        // Player 2 resolve the spell
        browser.player2().getActionHelper().clickContinue();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService(GameStatusUpdaterService gameStatusUpdaterService) {
            return new InitTestService(gameStatusUpdaterService) {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);

                    addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
                    addCardToNonCurrentPlayerBattlefield(gameStatus, HUATLIS_SNUBHORN);
                    addCardToNonCurrentPlayerBattlefield(gameStatus, COLOSSAL_DREADMAW);
                }
            };
        }
    }
}
