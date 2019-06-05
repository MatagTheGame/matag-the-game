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
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.sets.Ixalan.AIR_ELEMENTAL;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastCreatureTest.InitGameTestConfiguration.class})
public class CastCreatureTest extends AbstractApplicatonTest {
    @Test
    public void castCreature() {
        // When click on creature without paying the cost
        player1.getHandHelper(PLAYER).clickFirstCard(AIR_ELEMENTAL);

        // Then battlefields is still empty
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).toHaveSize(5);
        player2.getBattlefieldHelper(OPPONENT, FIRST_LINE).toHaveSize(5);

        // When clicking all lands
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(ISLAND, 0);
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(ISLAND, 1);
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(ISLAND, 2);
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(MOUNTAIN, 0);
        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).clickCard(MOUNTAIN, 1);

        // Then all lands are front-end tapped and creature is in the battlefield
//        // TODO FIXME
//        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).isFrontendTapped();
//        player1.getBattlefieldHelper(PLAYER, FIRST_LINE).contains(AIR_ELEMENTAL);
//        player2.getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(ISLAND, 0).isFrontendTapped();
//
//        player2.getBattlefieldHelper(PLAYER, FIRST_LINE).contains(AIR_ELEMENTAL);
//
//        // Hand is now empty
//        player1.getHandHelper(PLAYER).isEmpty();
//        player2.getHandHelper(PLAYER).isEmpty();
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
