package application.cast;

import application.AbstractApplicationTest;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
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
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.sets.Ixalan.HEADWATER_SENTRIES;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastCreatureTest.InitGameTestConfiguration.class})
public class CastCreatureTest extends AbstractApplicationTest {
    @Test
    public void castCreature() {
        // When click on creature without paying the cost
        browser.player1().getHandHelper(PLAYER).getFirstCard(HEADWATER_SENTRIES).click();

        // Then stack is still empty
        browser.player1().getStackHelper().toHaveSize(0);

        // When clicking all lands
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).click();

        // Then all lands are front-end tapped
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).isFrontendTapped();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).isFrontendTapped();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).isFrontendTapped();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).isFrontendTapped();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).isFrontendTapped();

        // When click on creature
        browser.player1().getHandHelper(PLAYER).getCard(HEADWATER_SENTRIES, 0).click();

        // Then all lands are tapped for both players
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).isTapped();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).isTapped();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).isTapped();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).isTapped();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).isTapped();

        browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(ISLAND, 0).isTapped();
        browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(ISLAND, 1).isTapped();
        browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(ISLAND, 2).isTapped();
        browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(MOUNTAIN, 0).isTapped();
        browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(MOUNTAIN, 1).isTapped();

        // And creature is on the stack for both players (hands is empty)
        browser.player1().getStackHelper().containsExactly(HEADWATER_SENTRIES);
        browser.player1().getHandHelper(PLAYER).isEmpty();
        browser.player2().getStackHelper().containsExactly(HEADWATER_SENTRIES);
        browser.player1().getHandHelper(OPPONENT).isEmpty();

        // And priority is to opponent with corresponding status
        browser.player1().getPhaseHelper().is(M1, OPPONENT);
        browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
        browser.player1().getActionHelper().cannotContinue();
        browser.player2().getPhaseHelper().is(M1, PLAYER);
        browser.player2().getStatusHelper().hasMessage("Play any instant or ability or resolve the top spell in the stack (SPACE).");
        browser.player2().getActionHelper().canContinue();

        // When player2 acknowledge the spell casted
        browser.player2().getActionHelper().clickContinue();

        // Creature goes on the battlefield (stack is empty)
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(HEADWATER_SENTRIES);
        browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(HEADWATER_SENTRIES);
        browser.player1().getStackHelper().isEmpty();
        browser.player2().getStackHelper().isEmpty();

        // And priority is to player again
        browser.player1().getPhaseHelper().is(M1, PLAYER);
        browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
        browser.player1().getActionHelper().canContinue();
        browser.player2().getPhaseHelper().is(M1, OPPONENT);
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
        browser.player2().getActionHelper().cannotContinue();

        // Hand is now empty
        browser.player1().getHandHelper(PLAYER).isEmpty();
        browser.player2().getHandHelper(PLAYER).isEmpty();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService() {
            return new InitTestService() {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerHand(gameStatus, HEADWATER_SENTRIES);
                    addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
                    addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
                    addCardToCurrentPlayerBattlefield(gameStatus, ISLAND);
                    addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
                    addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
                }
            };
        }
    }
}
