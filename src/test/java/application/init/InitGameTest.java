package application.init;

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
import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.Ixalan.CHARGING_MONSTROSAUR;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.cards.sets.Ixalan.HUATLIS_SNUBHORN;
import static com.aa.mtg.cards.sets.Ixalan.LEGIONS_JUDGMENT;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({InitGameTest.InitGameTestConfiguration.class})
public class InitGameTest extends AbstractApplicationTest {
    @Test
    public void display() {
        // Hands are
        browser.player1().getHandHelper(PLAYER).containsExactly(ISLAND, LEGIONS_JUDGMENT);
        browser.player1().getHandHelper(OPPONENT).containsExactly("card", "card");
        browser.player2().getHandHelper(PLAYER).containsExactly(FOREST, CHARGING_MONSTROSAUR);
        browser.player2().getHandHelper(OPPONENT).containsExactly("card", "card");

        // Battlefields are
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).containsExactly(PLAINS, PLAINS);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).containsExactly(GRAZING_WHIPTAIL);
        browser.player2().getBattlefieldHelper(OPPONENT, FIRST_LINE).containsExactly(PLAINS, PLAINS);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).containsExactly(GRAZING_WHIPTAIL);

        // Graveyards are
        browser.player1().getGraveyardHelper(PLAYER).containsExactly(PLAINS);
        browser.player1().getGraveyardHelper(OPPONENT).containsExactly(MOUNTAIN);
        browser.player2().getGraveyardHelper(PLAYER).containsExactly(MOUNTAIN);
        browser.player2().getGraveyardHelper(OPPONENT).containsExactly(PLAINS);

        // PlayerInfos are
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveName("Pippo");
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(20);
        browser.player1().getPlayerInfoHelper(PLAYER).toBeActive();
        browser.player1().getPlayerInfoHelper(OPPONENT).toHaveName("Pluto");
        browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(20);
        browser.player1().getPlayerInfoHelper(OPPONENT).toBeInactive();
        browser.player2().getPlayerInfoHelper(PLAYER).toHaveName("Pluto");
        browser.player2().getPlayerInfoHelper(PLAYER).toHaveLife(20);
        browser.player2().getPlayerInfoHelper(PLAYER).toBeInactive();
        browser.player2().getPlayerInfoHelper(OPPONENT).toHaveName("Pippo");
        browser.player2().getPlayerInfoHelper(OPPONENT).toHaveLife(20);
        browser.player2().getPlayerInfoHelper(OPPONENT).toBeActive();

        // Phase and statuses are
        browser.player1().getPhaseHelper().is(M1, PLAYER);
        browser.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).");
        browser.player1().getActionHelper().canContinue();
        browser.player2().getPhaseHelper().is(M1, OPPONENT);
        browser.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
        browser.player2().getActionHelper().cannotContinue();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService() {
            return new InitTestService() {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    // Current Player
                    addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
                    addCardToCurrentPlayerLibrary(gameStatus, ISLAND);

                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, HUATLIS_SNUBHORN);

                    addCardToCurrentPlayerHand(gameStatus, ISLAND);
                    addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);

                    addCardToCurrentPlayerGraveyard(gameStatus, PLAINS);

                    // Non Current Player
                    addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
                    addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);

                    addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
                    addCardToNonCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
                    addCardToNonCurrentPlayerBattlefield(gameStatus, GRAZING_WHIPTAIL);

                    addCardToNonCurrentPlayerHand(gameStatus, FOREST);
                    addCardToNonCurrentPlayerHand(gameStatus, CHARGING_MONSTROSAUR);

                    addCardToNonCurrentPlayerGraveyard(gameStatus, MOUNTAIN);
                }
            };
        }
    }
}
