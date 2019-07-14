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
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.cards.sets.Ixalan.NEST_ROBBER;
import static com.aa.mtg.cards.sets.RivalsOfIxalan.DEAD_WEIGHT;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastNegativeAuraOnOpponentCreatureTest.InitGameTestConfiguration.class})
public class CastNegativeAuraOnOpponentCreatureTest extends AbstractApplicationTest {
    @Test
    public void castNegativeAuraOnOpponentCreature() {
        // When cast an enchantment aura on a creature with toughness higher than 2
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 0).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(DEAD_WEIGHT).select();
        browser.player1().getStatusHelper().hasMessage("Select targets for Dead Weight.");
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(GRAZING_WHIPTAIL).click();

        // Enchantment goes on the stack
        browser.player1().getStackHelper().containsExactly(DEAD_WEIGHT);

        // When opponent accepts enchantment
        browser.player2().getActionHelper().clickContinue();

        // Then the attachment and its effect are on the battlefield
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(DEAD_WEIGHT);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(GRAZING_WHIPTAIL).hasPowerAndToughness("1/2");

        // When cast an enchantment aura on a creature with toughness equal 1
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 1).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(DEAD_WEIGHT).select();
        browser.player1().getStatusHelper().hasMessage("Select targets for Dead Weight.");
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(NEST_ROBBER).click();

        // Enchantment goes on the stack
        browser.player1().getStackHelper().containsExactly(DEAD_WEIGHT);

        // When opponent accepts enchantment
        browser.player2().getActionHelper().clickContinue();

        // Then the creature immediately dies
        browser.player1().getGraveyardHelper(PLAYER).contains(DEAD_WEIGHT);
        browser.player1().getGraveyardHelper(OPPONENT).contains(NEST_ROBBER);
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService() {
            return new InitTestService() {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerHand(gameStatus, DEAD_WEIGHT);
                    addCardToCurrentPlayerHand(gameStatus, DEAD_WEIGHT);
                    addCardToCurrentPlayerBattlefield(gameStatus, SWAMP);
                    addCardToCurrentPlayerBattlefield(gameStatus, SWAMP);

                    addCardToNonCurrentPlayerBattlefield(gameStatus, GRAZING_WHIPTAIL);
                    addCardToNonCurrentPlayerBattlefield(gameStatus, NEST_ROBBER);
                }
            };
        }
    }
}
