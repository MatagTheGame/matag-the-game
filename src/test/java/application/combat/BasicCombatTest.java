package application.combat;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
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
import static com.aa.mtg.cards.sets.Ixalan.ANCIENT_BRONTODON;
import static com.aa.mtg.cards.sets.Ixalan.HEADWATER_SENTRIES;
import static com.aa.mtg.cards.sets.Ixalan.NEST_ROBBER;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.AXEBANE_BEAST;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CORAL_COMMANDO;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.game.turn.phases.AfterDeclareBlockersPhase.AB;
import static com.aa.mtg.game.turn.phases.BeginCombatPhase.BC;
import static com.aa.mtg.game.turn.phases.DeclareAttackersPhase.DA;
import static com.aa.mtg.game.turn.phases.DeclareBlockersPhase.DB;
import static com.aa.mtg.game.turn.phases.Main2Phase.M2;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({BasicCombatTest.InitGameTestConfiguration.class})
public class BasicCombatTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new BasicCombatTest.InitTestServiceForTest());
    }

    @Test
    public void basicCombat() {
        // When continuing
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getPhaseHelper().is(BC, PLAYER);
        browser.player2().getActionHelper().clickContinue();

        // Message and status are about declaring attackers
        browser.player1().getPhaseHelper().is(DA, PLAYER);
        browser.player1().getStatusHelper().hasMessage("Choose creatures you want to attack with.");

        // When declare attacker
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HEADWATER_SENTRIES).click();

        // Then attacker is moved forward
        browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(HEADWATER_SENTRIES).isFrontendTapped();

        // When withdraw attacker
        browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(HEADWATER_SENTRIES).click();

        // Then attacker is moved backward
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HEADWATER_SENTRIES).isNotTapped();

        // When declare illegal attacker
        int nestRobberId = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(NEST_ROBBER).getCardIdNumeric();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(NEST_ROBBER).click();

        // Then a message is displayed
        browser.getMessageHelper().hasMessage("\"" + nestRobberId + " - Nest Robber\" is already tapped and cannot attack.");
        browser.getMessageHelper().close();

        // The four attackers are declared as attacker and continue
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HEADWATER_SENTRIES).click();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CORAL_COMMANDO).click();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(AXEBANE_BEAST).click();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(ANCIENT_BRONTODON).click();
        browser.player1().getActionHelper().clickContinue();

        // Priority passes to the opponent
        browser.player1().getPhaseHelper().is(DA, OPPONENT);
        browser.player2().getPhaseHelper().is(DA, PLAYER);

        // When opponent click on a card nothing happens
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(NEST_ROBBER).click();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(NEST_ROBBER);

        // Opponent continue
        browser.player2().getActionHelper().clickContinue();

        // The phase move to Declare blocker
        browser.player1().getPhaseHelper().is(DB, OPPONENT);
        browser.player1().getStatusHelper().hasMessage("Wait for opponent to perform its action...");
        browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).toHaveSize(4);
        browser.player1().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(HEADWATER_SENTRIES).isTapped();

        // And the opponent sees the same
        browser.player2().getPhaseHelper().is(DB, PLAYER);
        browser.player2().getStatusHelper().hasMessage("Choose creatures you want to block with.");
        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).toHaveSize(4);
        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(HEADWATER_SENTRIES).isTapped();

        // Opponent select a creature to block
        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(ANCIENT_BRONTODON).click();
        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(ANCIENT_BRONTODON).isSelected();

        // Opponent decide to block that creature
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CORAL_COMMANDO).click();
        browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).contains(CORAL_COMMANDO);

        // Opponent decide to withdraw that block
        browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(CORAL_COMMANDO).click();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(CORAL_COMMANDO);

        // Opponent declare blockers
        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(HEADWATER_SENTRIES).click();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HEADWATER_SENTRIES).click();
        browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(HEADWATER_SENTRIES).parentHasStyle("margin-left: 390px; margin-top: 0px;");

        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(CORAL_COMMANDO).click();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CORAL_COMMANDO).click();
        browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(CORAL_COMMANDO).parentHasStyle("margin-left: 130px; margin-top: 0px;");

        browser.player2().getBattlefieldHelper(OPPONENT, COMBAT_LINE).getFirstCard(AXEBANE_BEAST).click();
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(NEST_ROBBER).click();
        browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getFirstCard(NEST_ROBBER).parentHasStyle("margin-left: -130px; margin-top: 0px;");
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HEADWATER_SENTRIES).click();
        browser.player2().getBattlefieldHelper(PLAYER, COMBAT_LINE).getCard(HEADWATER_SENTRIES, 1).parentHasStyle("margin-left: -105px; margin-top: 50px;");

        // And continue
        browser.player2().getActionHelper().clickContinue();

        // Phase is moved to AB for Player1
        browser.player1().getPhaseHelper().is(AB, PLAYER);
        browser.player2().getPhaseHelper().is(AB, OPPONENT);

        // And continue
        browser.player1().getActionHelper().clickContinue();

        // Phase is moved to AB for Player2
        browser.player1().getPhaseHelper().is(AB, OPPONENT);
        browser.player2().getPhaseHelper().is(AB, PLAYER);

        // And continue
        browser.player2().getActionHelper().clickContinue();

        // Phase is moved to M2
        browser.player2().getPhaseHelper().is(M2, OPPONENT);
        browser.player1().getPhaseHelper().is(M2, PLAYER);

        // Cards are now
        browser.player1().getGraveyardHelper(PLAYER).contains(CORAL_COMMANDO, AXEBANE_BEAST);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(HEADWATER_SENTRIES, ANCIENT_BRONTODON);
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(HEADWATER_SENTRIES).hasDamage(2);
        browser.player1().getGraveyardHelper(OPPONENT).contains(CORAL_COMMANDO, NEST_ROBBER);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(HEADWATER_SENTRIES, HEADWATER_SENTRIES);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getCard(HEADWATER_SENTRIES, 0).hasDamage(2);
        browser.player1().getBattlefieldHelper(OPPONENT, SECOND_LINE).getCard(HEADWATER_SENTRIES, 1).hasDamage(2);

        browser.player2().getGraveyardHelper(OPPONENT).contains(CORAL_COMMANDO, AXEBANE_BEAST);
        browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).contains(HEADWATER_SENTRIES, ANCIENT_BRONTODON);
        browser.player2().getBattlefieldHelper(OPPONENT, SECOND_LINE).getFirstCard(HEADWATER_SENTRIES).hasDamage(2);
        browser.player2().getGraveyardHelper(PLAYER).contains(CORAL_COMMANDO, NEST_ROBBER);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(HEADWATER_SENTRIES, HEADWATER_SENTRIES);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(HEADWATER_SENTRIES, 0).hasDamage(2);
        browser.player2().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(HEADWATER_SENTRIES, 1).hasDamage(2);

        // Life is
        browser.player2().getPlayerInfoHelper(PLAYER).toHaveLife(11);
        browser.player2().getPlayerInfoHelper(OPPONENT).toHaveLife(20);

        browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(20);
        browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(11);
    }

    @Configuration
    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            // Single block both survive
            addCardToCurrentPlayerBattlefield(gameStatus, HEADWATER_SENTRIES); // 2/5
            addCardToNonCurrentPlayerBattlefield(gameStatus, HEADWATER_SENTRIES); // 2/5

            // Single block both die
            addCardToCurrentPlayerBattlefield(gameStatus, CORAL_COMMANDO); // 3/2
            addCardToNonCurrentPlayerBattlefield(gameStatus, CORAL_COMMANDO); // 3/2

            // Double block attacker dies one blocker dies
            addCardToCurrentPlayerBattlefield(gameStatus, AXEBANE_BEAST); // 3/4
            addCardToNonCurrentPlayerBattlefield(gameStatus, NEST_ROBBER); // 2/1
            addCardToNonCurrentPlayerBattlefield(gameStatus, HEADWATER_SENTRIES); // 2/5

            // Non blocked damage to player
            addCardToCurrentPlayerBattlefield(gameStatus, ANCIENT_BRONTODON); // 9/9

            // Cannot attack as tapped
            addCardToCurrentPlayerBattlefield(gameStatus, NEST_ROBBER);
            gameStatus.getCurrentPlayer().getBattlefield().getCards().get(4).getModifiers().tap();
        }
    }
}
