package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import application.testcategory.Regression;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.COMBAT_LINE;
import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.sets.Ixalan.CHARGING_MONSTROSAUR;
import static com.aa.mtg.cards.sets.Ixalan.HUATLIS_SNUBHORN;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CombatTrampleHasteTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class CombatTrampleHasteTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CombatTrampleHasteTest.InitTestServiceForTest());
    }

    @Test
    public void combatTrampleHaste() {
        // Playing card with trample haste
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 2).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 3).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 4).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(CHARGING_MONSTROSAUR).click();
        browser.player2().getActionHelper().clickContinue();

        // When going to combat
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getPhaseHelper().is(BC, PLAYER);
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getPhaseHelper().is(DA, PLAYER);

        // When declare attacker
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CHARGING_MONSTROSAUR).declareAsAttacker();
        browser.player1().getActionHelper().clickContinue();

        // Declare blocker
        browser.player2().getPhaseHelper().is(DA, PLAYER);
        browser.player2().getActionHelper().clickContinue();
        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(CHARGING_MONSTROSAUR).select();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HUATLIS_SNUBHORN).declareAsBlocker();
        browser.player2().getActionHelper().clickContinue();

        // No instant played during combat
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();

        // Then
        browser.player1().getPhaseHelper().is(M2, PLAYER);
        browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(17);

        browser.player1().getGraveyardHelper(OPPONENT).contains(HUATLIS_SNUBHORN);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CHARGING_MONSTROSAUR).hasDamage(2);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerHand(gameStatus, CHARGING_MONSTROSAUR);
            addCardToNonCurrentPlayerBattlefield(gameStatus, HUATLIS_SNUBHORN);
        }
    }
}
