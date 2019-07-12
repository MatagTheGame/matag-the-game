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
import static com.aa.mtg.cards.sets.Dominaria.SHORT_SWORD;
import static com.aa.mtg.cards.sets.Ixalan.LEGIONS_JUDGMENT;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.CONCORDIA_PEGASUS;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastEquipmentDestroyCreatureTest.InitGameTestConfiguration.class})
public class CastEquipmentDestroyCreatureTest extends AbstractApplicationTest {
    @Test
    public void castAuraDestroyCreature() {
        // When cast an enchantment aura
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(SHORT_SWORD).click();

        // Enchantment goes on
        browser.player1().getStackHelper().containsExactly(SHORT_SWORD);

        // When opponent accepts enchantment
        browser.player2().getActionHelper().clickContinue();

        // Then the attachment and its effect are on the battlefield
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(SHORT_SWORD);

        // When casting another instance of the same aura
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 2).tap();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(SHORT_SWORD).select();
//        browser.player1().getStatusHelper().hasMessage("Select targets for Short Sword.");
//        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).click();
//
//        // Enchantment goes on
//        browser.player1().getStackHelper().containsExactly("Attach");
//
//        // When opponent accepts enchantment
//        browser.player2().getActionHelper().clickContinue();
//
//        // Then the attachment and its effect are on the battlefield
//        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(CANDLELIGHT_VIGIL);
//        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).hasPowerAndToughness("7/7");
//
//        // Verify as well the vigilance effect
//        browser.player1().getActionHelper().clickContinue();
//        browser.getPhaseHelper().is(DA, PLAYER);
//        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).declareAsAttacker();
//        browser.player1().getActionHelper().clickContinue();
//        browser.player1().getPhaseHelper().is(M2, PLAYER);
//        browser.player1().getPlayerInfoHelper(OPPONENT).toHaveLife(13);
//        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).isNotTapped();
//
//        // Destroy the creature
//        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 3).tap();
//        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 4).tap();
//        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 5).tap();
//        browser.player1().getHandHelper(PLAYER).getFirstCard(LEGIONS_JUDGMENT).select();
//        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getFirstCard(CONCORDIA_PEGASUS).click();
//        browser.player2().getActionHelper().clickContinue();
//
//        // Creature and its enchantments are in the graveyard
//        browser.player1().getGraveyardHelper(PLAYER).contains(CONCORDIA_PEGASUS, CANDLELIGHT_VIGIL, CANDLELIGHT_VIGIL, LEGIONS_JUDGMENT);
//        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).isEmpty();
    }

    @Configuration
    static class InitGameTestConfiguration {
        @Bean
        public InitTestService initTestService() {
            return new InitTestService() {
                @Override
                protected void initGameStatus(GameStatus gameStatus) {
                    addCardToCurrentPlayerBattlefield(gameStatus, CONCORDIA_PEGASUS);
                    addCardToCurrentPlayerHand(gameStatus, LEGIONS_JUDGMENT);
                    addCardToCurrentPlayerHand(gameStatus, SHORT_SWORD);
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
