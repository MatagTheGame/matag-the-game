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

import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.CoreSet2020.BARTIZAN_BATS;
import static com.aa.mtg.cards.sets.CoreSet2020.BASTION_ENFORCER;
import static com.aa.mtg.cards.sets.CoreSet2020.DARK_REMEDY;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastInstantPoweringCreatureDuringCombatTest.InitGameTestConfiguration.class})
public class CastInstantPoweringCreatureDuringCombatTest extends AbstractApplicationTest {
    @Test
    public void castInstantPoweringCreatureDuringCombat() {
        // When player one attack thinking to win the fight
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getPhaseHelper().is(BC, PLAYER);
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getPhaseHelper().is(DA, PLAYER);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(BASTION_ENFORCER).declareAsAttacker();
        browser.player1().getActionHelper().clickContinue();

        // And player 2 plays the game by blocking
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(BARTIZAN_BATS).declareAsBlocker();
        browser.player2().getActionHelper().clickContinue();

        // Between DA and DB no spell should be allowed... not true, blockers could give flying.
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService() {
            return new InitTestService() {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerBattlefield(gameStatus, BASTION_ENFORCER);

                    addCardToNonCurrentPlayerHand(gameStatus, DARK_REMEDY);
                    addCardToNonCurrentPlayerBattlefield(gameStatus, BARTIZAN_BATS);
                    addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
                    addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
                }
            };
        }
    }
}
