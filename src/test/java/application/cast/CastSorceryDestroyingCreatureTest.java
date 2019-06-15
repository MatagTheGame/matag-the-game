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
import static java.util.Collections.singletonList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastSorceryDestroyingCreatureTest.InitGameTestConfiguration.class})
public class CastSorceryDestroyingCreatureTest extends AbstractApplicationTest {
    @Test
    public void castCreature() {
        // When clicking all lands
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).click();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).click();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).click();

        // When click on a sorcery that requires target
        int legionsJudgmentId = player1.getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).getCardIdNumeric();
        player1.getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then card is selected and message ask to choose a target
        player1.getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).hasClass("selected");
        player1.getStatusHelper().hasMessage("Select targets for Legion's Judgment.");

        // When un-selecting the card
        player1.getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then card is unselected
        player1.getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).doesNotHaveClass("selected");
        player1.getStatusHelper().hasMessage("Play any spell or abilities or continue (SPACE).");

        // When click on a sorcery that requires target
        player1.getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();

        // Then again card is selected and message ask to choose a target
        player1.getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).hasClass("selected");
        player1.getStatusHelper().hasMessage("Select targets for Legion's Judgment.");

        // When clicking a wrong target
        player1.getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).click();

        // The card is still on the hand selected
        player1.getMessageHelper().hasMessage("Selected targets were not valid.");
        player1.getMessageHelper().close();
        player1.getMessageHelper().hasNoMessage();

        // When clicking again on the sorcery and on a valid target
        player1.getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).click();
        player1.getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(COLOSSAL_DREADMAW).click();

        // Then spell goes on the stack
        player1.getStackHelper().containsAbilitiesExactly(singletonList("Pippo's Legion's Judgment (" + legionsJudgmentId + "): Destroy target."));
        player2.getStackHelper().containsAbilitiesExactly(singletonList("Pippo's Legion's Judgment (" + legionsJudgmentId + "): Destroy target."));

        // TODO continue
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
