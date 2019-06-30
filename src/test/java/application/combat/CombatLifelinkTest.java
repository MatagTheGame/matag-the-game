package application.combat;

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
import static com.aa.mtg.cards.sets.WarOfTheSparks.CHARITY_EXTRACTOR;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CombatLifelinkTest.InitGameTestConfiguration.class})
public class CombatLifelinkTest extends AbstractApplicationTest {
    @Test
    public void combatLifelink() {
        // When going to combat
        browser.player1().getActionHelper().clickContinue();
        browser.player1().getPhaseHelper().is(DA, PLAYER);

        // When declare attacker
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CHARITY_EXTRACTOR).declareAsAttacker();
        browser.player1().getActionHelper().clickContinue();

        // Declare blocker
        browser.player1().getPhaseHelper().is(M2, PLAYER);
        browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(19);
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(21);
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService() {
            return new InitTestService() {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerBattlefield(gameStatus, CHARITY_EXTRACTOR);
                }
            };
        }
    }
}
