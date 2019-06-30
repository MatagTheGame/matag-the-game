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
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.GuildsOfRavnica.CANDLELIGHT_VIGIL;
import static com.aa.mtg.cards.sets.Ixalan.HUATLIS_SNUBHORN;
import static com.aa.mtg.cards.sets.Ixalan.LEGIONS_JUDGMENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastAuraDestroyCreatureTest.InitGameTestConfiguration.class})
public class CastAuraDestroyCreatureTest extends AbstractApplicationTest {
    @Test
    public void castAuraDestroyCreature() {
        // When cast an enchantment aura
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 2).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 3).click();
        browser.player1().getHandHelper(PLAYER).getFirstCard(CANDLELIGHT_VIGIL).click();
        browser.player1().getHandHelper(PLAYER).getFirstCard(CANDLELIGHT_VIGIL).isSelected();
        browser.player1().getStatusHelper().hasMessage("Select targets for Candlelight Vigil.");
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).click();

        // Enchantment goes on
        browser.player1().getStackHelper().containsExactly(CANDLELIGHT_VIGIL);

        // When opponent accept enchantment
        browser.player2().getActionHelper().clickContinue();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService() {
            return new InitTestService() {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerBattlefield(gameStatus, HUATLIS_SNUBHORN);
                    addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);
                    addCardToCurrentPlayerHand(gameStatus, CANDLELIGHT_VIGIL);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                    addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
                }
            };
        }
    }
}
