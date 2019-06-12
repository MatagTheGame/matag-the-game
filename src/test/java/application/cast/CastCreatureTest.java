package application.cast;

import application.AbstractApplicatonTest;
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
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.sets.Ixalan.AIR_ELEMENTAL;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastCreatureTest.InitGameTestConfiguration.class})
public class CastCreatureTest extends AbstractApplicatonTest {
    @Test
    public void castCreature() {
        // When click on creature without paying the cost
        player1.getHandHelper(PLAYER).clickFirstCard(AIR_ELEMENTAL);

        // Then stack is still empty
        player1.getStackHelper().toHaveSize(0);

        // When clicking all lands
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(ISLAND, 0);
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(ISLAND, 1);
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(ISLAND, 2);
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(MOUNTAIN, 0);
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(MOUNTAIN, 1);

        // Then all lands are front-end tapped
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).isFrontendTapped();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).isFrontendTapped();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).isFrontendTapped();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).isFrontendTapped();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).isFrontendTapped();

        // When click on creature
        player1.getHandHelper(PLAYER).clickCard(AIR_ELEMENTAL, 0);

        // Then all lands are tapped for both players
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).isTapped();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 1).isTapped();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 2).isTapped();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).isTapped();
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).isTapped();

        player2.getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(ISLAND, 0).isTapped();
        player2.getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(ISLAND, 1).isTapped();
        player2.getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(ISLAND, 2).isTapped();
        player2.getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(MOUNTAIN, 0).isTapped();
        player2.getBattlefieldHelper(OPPONENT, FIRST_LINE).getCard(MOUNTAIN, 1).isTapped();

        // And creature is on the stack for both players (hands is empty)
        player1.getStackHelper().contains(AIR_ELEMENTAL);
        player1.getHandHelper(PLAYER).isEmpty();
        player2.getStackHelper().contains(AIR_ELEMENTAL);
        player1.getHandHelper(OPPONENT).isEmpty();

        // And priority is to opponent with corresponding status
        player1.getPhaseHelper().is(M1, OPPONENT);
        player1.getStatusHelper().hasMessage("Wait for opponent to perform its action...");
        player1.getActionHelper().cannotContinue();
        player2.getPhaseHelper().is(M1, PLAYER);
        player2.getStatusHelper().hasMessage("Play any instant or abilities or resolve the top spell in the stack (SPACE).");
        player2.getActionHelper().canContinue();

        // When player2 acknowledge the spell casted
        player2.getActionHelper().clickContinue();

        // Creature goes on the battlefield (stack is empty)
        player1.getBattlefieldHelper(PLAYER, SECOND_LINE).contains(AIR_ELEMENTAL);
        player2.getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(AIR_ELEMENTAL);
        player1.getStackHelper().isEmpty();
        player2.getStackHelper().isEmpty();

        // And priority is to player again
        player1.getPhaseHelper().is(M1, PLAYER);
        player1.getStatusHelper().hasMessage("Play any spell or abilities or continue (SPACE).");
        player1.getActionHelper().canContinue();
        player2.getPhaseHelper().is(M1, OPPONENT);
        player2.getStatusHelper().hasMessage("Wait for opponent to perform its action...");
        player2.getActionHelper().cannotContinue();

        // Hand is now empty
        player1.getHandHelper(PLAYER).isEmpty();
        player2.getHandHelper(PLAYER).isEmpty();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService(GameStatusUpdaterService gameStatusUpdaterService) {
            return new InitTestService(gameStatusUpdaterService) {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerHand(gameStatus, AIR_ELEMENTAL);
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
