package application.cleanup;

import application.AbstractApplicationTest;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.cards.CardInstance;
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
import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.modifiers.PowerToughness.powerToughness;
import static com.aa.mtg.cards.modifiers.TappedModifier.TAPPED;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.cards.sets.Ixalan.HUATLIS_SNUBHORN;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.Main1Phase.M1;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CleanupTest.InitGameTestConfiguration.class})
public class CleanupTest extends AbstractApplicationTest {
    @Test
    public void cleanupWhenPassingTurns() {
        // Battlefields is
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).hasDamage(1);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).isTapped();

        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GRAZING_WHIPTAIL).hasSummoningSickness();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GRAZING_WHIPTAIL).hasPowerAndToughness("4/5");

        // Phase is
        browser.player1().getPhaseHelper().is(M1, PLAYER);

        // When Player1 clicks continue
        browser.player1().getActionHelper().clickContinue();

        // Phase is
        browser.player1().getPhaseHelper().is(M2, PLAYER);

        // When Player1 clicks continue
        browser.player1().getActionHelper().clickContinue();

        // Phase is
        browser.player1().getPhaseHelper().is(M1, OPPONENT);

        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).doesNotHaveDamage();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).isTapped();

        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GRAZING_WHIPTAIL).hasSummoningSickness();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GRAZING_WHIPTAIL).hasPowerAndToughness("3/4");

        // When Player2 clicks continue twice
        browser.player2().getActionHelper().clickContinue();
        browser.player2().getPhaseHelper().is(M2, PLAYER);
        browser.player2().getActionHelper().clickContinue();

        // Phase is
        browser.player1().getPhaseHelper().is(M1, PLAYER);

        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).isNotTapped();
//        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GRAZING_WHIPTAIL).doesNotHAveSummoningSickness();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService() {
            return new InitTestService() {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    // Current Player
                    addCardToCurrentPlayerLibrary(gameStatus, ISLAND);

                    addCardToCurrentPlayerBattlefield(gameStatus, HUATLIS_SNUBHORN);
                    addCardToCurrentPlayerBattlefield(gameStatus, GRAZING_WHIPTAIL);

                    CardInstance huatlisShubhorn = gameStatus.getCurrentPlayer().getBattlefield().getCards().get(0);
                    huatlisShubhorn.getModifiers().setTapped(TAPPED);
                    huatlisShubhorn.getModifiers().dealDamage(1);

                    CardInstance grazingWhiptail = gameStatus.getCurrentPlayer().getBattlefield().getCards().get(1);
                    grazingWhiptail.getModifiers().setSummoningSickness(true);
                    grazingWhiptail.getModifiers().setExtraPowerToughnessUntilEndOfTurn(powerToughness("1/1"));

                    // Non Current Player
                    addCardToNonCurrentPlayerLibrary(gameStatus, MOUNTAIN);
                }
            };
        }
    }
}
