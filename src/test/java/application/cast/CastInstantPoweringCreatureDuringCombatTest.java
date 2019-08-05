package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.COMBAT_LINE;
import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.Cards.SWAMP;
import static com.aa.mtg.cards.sets.CoreSet2020.BASTION_ENFORCER;
import static com.aa.mtg.cards.sets.CoreSet2020.DARK_REMEDY;
import static com.aa.mtg.cards.sets.GuildsOfRavnica.BARTIZAN_BATS;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;
import static com.aa.mtg.game.turn.phases.UpkeepPhase.UP;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastInstantPoweringCreatureDuringCombatTest.InitGameTestConfiguration.class})
public class CastInstantPoweringCreatureDuringCombatTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CastInstantPoweringCreatureDuringCombatTest.InitTestServiceForTest());
    }

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
        browser.player2().getActionHelper().clickContinue();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(BARTIZAN_BATS).declareAsBlocker();
        browser.player2().getActionHelper().clickContinue();

        // And player 1 don't play anything
        browser.player1().getPhaseHelper().is(AB, PLAYER);
        browser.player1().getActionHelper().clickContinue();

        // Then player 2 can pump up its creature
        browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 0).tap();
        browser.player2().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(SWAMP, 1).tap();
        browser.player2().getPhaseHelper().is(AB, PLAYER);
        browser.player2().getHandHelper(PLAYER).getFirstCard(DARK_REMEDY).click();
        browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(BARTIZAN_BATS).click();

        // And spell goes on the stack
        browser.player2().getStackHelper().contains(DARK_REMEDY);
        browser.player1().getStackHelper().contains(DARK_REMEDY);

        // When player 1 continue
        browser.player1().getPhaseHelper().is(AB, PLAYER);
        browser.player1().getActionHelper().clickContinue();

        // Then spell is resolved from the stack and creature gets the +1/+3
        browser.player1().getStackHelper().isEmpty();
        browser.player1().getGraveyardHelper(OPPONENT).contains(DARK_REMEDY);
        browser.player1().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(BARTIZAN_BATS).hasPowerAndToughness("4/4");
        browser.player1().getPhaseHelper().is(AB, OPPONENT);
        browser.player2().getStackHelper().isEmpty();
        browser.player2().getGraveyardHelper(PLAYER).contains(DARK_REMEDY);
        browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(BARTIZAN_BATS).hasPowerAndToughness("4/4");
        browser.player2().getPhaseHelper().is(AB, PLAYER);

        // When player 2 continues
        browser.player2().getActionHelper().clickContinue();

        // Then combat is ended
        browser.player2().getPhaseHelper().is(M2, OPPONENT);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(BARTIZAN_BATS).hasDamage(3);
        browser.player2().getGraveyardHelper(OPPONENT).contains(BASTION_ENFORCER);

        // When moving to next turn
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();

        // Then extra power and toughness is cleaned up
        browser.player2().getPhaseHelper().is(UP, PLAYER);
        browser.getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(BARTIZAN_BATS).hasPowerAndToughness("3/1");
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, BASTION_ENFORCER);

            addCardToNonCurrentPlayerHand(gameStatus, DARK_REMEDY);
            addCardToNonCurrentPlayerBattlefield(gameStatus, BARTIZAN_BATS);
            addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
            addCardToNonCurrentPlayerBattlefield(gameStatus, SWAMP);
        }
    }
}
