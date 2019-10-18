package application.enter;

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

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static application.browser.BattlefieldHelper.SECOND_LINE;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.sets.CoreSet2019.AJANIS_WELCOME;
import static com.aa.mtg.cards.sets.CoreSet2019.DAYBREAK_CHAPLAIN;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({WheneverACreatureEntersTheBattlefieldAbilityTest.InitGameTestConfiguration.class})
@Category(Regression.class)
public class WheneverACreatureEntersTheBattlefieldAbilityTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new WheneverACreatureEntersTheBattlefieldAbilityTest.InitTestServiceForTest());
    }

    @Test
    public void wheneverACreatureEntersTheBattlefieldAbility() {
        // When Playing Ajanis Welcome
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 0).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(AJANIS_WELCOME).click();
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(AJANIS_WELCOME);

        // And then a creature
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 1).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 2).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(DAYBREAK_CHAPLAIN).click();
        browser.player2().getActionHelper().clickContinue();

        // Then a the creature is on the battlefield and event is triggered
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(DAYBREAK_CHAPLAIN);
        int ajanisWelcomeId1 = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(AJANIS_WELCOME, 0).getCardIdNumeric();
        browser.player1().getStackHelper().containsAbility("Pippo's Ajanis Welcome (" + ajanisWelcomeId1 + "): Gain 1 life.");

        // When players continue
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();

        // Then player 1 gains 1 life
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(21);

        // When Playing another Ajanis Welcome
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 3).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(AJANIS_WELCOME).click();
        browser.player2().getActionHelper().clickContinue();
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(AJANIS_WELCOME);

        // And then another creature
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 4).tap();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(PLAINS, 5).tap();
        browser.player1().getHandHelper(PLAYER).getFirstCard(DAYBREAK_CHAPLAIN).click();
        browser.player2().getActionHelper().clickContinue();

        // Then a both creatures are on the battlefield two events is triggered
        browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).contains(DAYBREAK_CHAPLAIN, DAYBREAK_CHAPLAIN);
        int ajanisWelcomeId2 = browser.player1().getBattlefieldHelper(PLAYER, SECOND_LINE).getCard(AJANIS_WELCOME, 1).getCardIdNumeric();
        browser.player1().getStackHelper().containsAbilitiesExactly(asList(
                "Pippo's Ajanis Welcome (" + ajanisWelcomeId1 + "): Gain 1 life.",
                "Pippo's Ajanis Welcome (" + ajanisWelcomeId2 + "): Gain 1 life."
        ));

        // When players continue
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();

        // Then player 1 gains 1 life
        browser.player1().getStackHelper().containsAbility("Pippo's Ajanis Welcome (" + ajanisWelcomeId1 + "): Gain 1 life.");
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(22);

        // When players continue
        browser.player1().getActionHelper().clickContinue();
        browser.player2().getActionHelper().clickContinue();

        // Then player 1 gains 1 life
        browser.player1().getPlayerInfoHelper(PLAYER).toHaveLife(23);
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, AJANIS_WELCOME);
            addCardToCurrentPlayerHand(gameStatus, AJANIS_WELCOME);
            addCardToCurrentPlayerHand(gameStatus, DAYBREAK_CHAPLAIN);
            addCardToCurrentPlayerHand(gameStatus, DAYBREAK_CHAPLAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
            addCardToCurrentPlayerBattlefield(gameStatus, PLAINS);
        }
    }
}
