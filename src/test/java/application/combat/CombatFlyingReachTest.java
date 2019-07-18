package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.browser.CardHelper;
import application.init.InitGameTest;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.COMBAT_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.sets.Ixalan.AIR_ELEMENTAL;
import static com.aa.mtg.cards.sets.Ixalan.ANCIENT_BRONTODON;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.DeclareBlockersPhase.DB;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CombatFlyingReachTest.InitGameTestConfiguration.class})
public class CombatFlyingReachTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CombatFlyingReachTest.InitTestServiceForTest());
    }

    @Test
    public void combatFlyingReach() {
        // When going to combat
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getPhaseHelper().is(BC, PLAYER);
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getPhaseHelper().is(DA, PLAYER);

        // creature with flying should have the correct class
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(AIR_ELEMENTAL).hasFlying();

        // When declare attacker
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(AIR_ELEMENTAL).declareAsAttacker();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(AIR_ELEMENTAL).declareAsAttacker();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(AIR_ELEMENTAL).declareAsAttacker();
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getPhaseHelper().is(DA, PLAYER);
        browser.player2().getActionHelper().clickContinue();

        // Declare blocker
        browser.player2().getPhaseHelper().is(DB, PLAYER);
        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getCard(AIR_ELEMENTAL, 0).click();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(AIR_ELEMENTAL).click();

        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getCard(AIR_ELEMENTAL, 1).click();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(GRAZING_WHIPTAIL).click();

        CardHelper airElemental = browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getCard(AIR_ELEMENTAL, 2);
        airElemental.click();
        CardHelper ancientBrontodon = browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(ANCIENT_BRONTODON);
        ancientBrontodon.click();
        browser.player2().getMessageHelper().hasMessage("\"" + ancientBrontodon.getCardIdNumeric() + " - Ancient Brontodon\" cannot block \"" + airElemental.getCardIdNumeric() + " - Air Elemental\" as it has flying.");
    }

    @Configuration
    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, AIR_ELEMENTAL);
            addCardToCurrentPlayerBattlefield(gameStatus, AIR_ELEMENTAL);
            addCardToCurrentPlayerBattlefield(gameStatus, AIR_ELEMENTAL);

            addCardToNonCurrentPlayerBattlefield(gameStatus, AIR_ELEMENTAL);
            addCardToNonCurrentPlayerBattlefield(gameStatus, GRAZING_WHIPTAIL);
            addCardToNonCurrentPlayerBattlefield(gameStatus, ANCIENT_BRONTODON);
        }
    }
}
