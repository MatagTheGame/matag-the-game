package application.init;

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

import static application.browser.CardHelper.cardNames;
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
import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({InitGameTest.InitGameTestConfiguration.class})
public class InitGameTest extends AbstractApplicatonTest {
    @Test
    public void display() {
        // Hands are
        player1.getHandHelper(PLAYER).containsExactly(cardNames(ISLAND, LEGIONS_JUDGMENT));
        player1.getHandHelper(OPPONENT).containsExactly(asList("card", "card"));
        player2.getHandHelper(PLAYER).containsExactly(cardNames(FOREST, CHARGING_MONSTROSAUR));
        player2.getHandHelper(OPPONENT).containsExactly(asList("card", "card"));

        // Battlefields are
        player1.getBattlefieldHelper(PLAYER).containsExactly(cardNames(PLAINS, HUATLIS_SNUBHORN));
        player1.getBattlefieldHelper(OPPONENT).containsExactly(cardNames(MOUNTAIN, GRAZING_WHIPTAIL));
        player2.getBattlefieldHelper(PLAYER).containsExactly(cardNames(MOUNTAIN, GRAZING_WHIPTAIL));
        player2.getBattlefieldHelper(OPPONENT).containsExactly(cardNames(PLAINS, HUATLIS_SNUBHORN));

        // Graveyards are
        player1.getGraveyardHelper(PLAYER).containsExactly(cardNames(PLAINS));
        player1.getGraveyardHelper(OPPONENT).containsExactly(cardNames(MOUNTAIN));
        player2.getGraveyardHelper(PLAYER).containsExactly(cardNames(MOUNTAIN));
        player2.getGraveyardHelper(OPPONENT).containsExactly(cardNames(PLAINS));

        // PlayerInfos are
        player1.getPlayerInfoHelper(PLAYER).toHaveName("Pippo");
        player1.getPlayerInfoHelper(PLAYER).toHaveLife("20");
        player1.getPlayerInfoHelper(PLAYER).toBeActive();
        player1.getPlayerInfoHelper(OPPONENT).toHaveName("Pluto");
        player1.getPlayerInfoHelper(OPPONENT).toHaveLife("20");
        player1.getPlayerInfoHelper(OPPONENT).toBeInactive();
        player2.getPlayerInfoHelper(PLAYER).toHaveName("Pluto");
        player2.getPlayerInfoHelper(PLAYER).toHaveLife("20");
        player2.getPlayerInfoHelper(PLAYER).toBeInactive();
        player2.getPlayerInfoHelper(OPPONENT).toHaveName("Pippo");
        player2.getPlayerInfoHelper(OPPONENT).toHaveLife("20");
        player2.getPlayerInfoHelper(OPPONENT).toBeActive();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService(GameStatusUpdaterService gameStatusUpdaterService) {
            return new InitTestService(gameStatusUpdaterService) {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    // Current Player
                    addCardToCurrentPlayerLibrary(gameStatus, PLAINS);
                    addCardToCurrentPlayerLibrary(gameStatus, ISLAND);

                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, HUATLIS_SNUBHORN);

                    addCardToCurrentPlayerHand(gameStatus, ISLAND);
                    addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);

                    addCardToCurrentPlayerGraveyard(gameStatus, PLAINS);

                    // Non Current Player
                    addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
                    addCardToNonCurrentPlayerLibrary(gameStatus, FOREST);

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
