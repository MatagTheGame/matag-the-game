package application.cast;

import application.AbstractApplicationTest;
import application.InitTestServiceDecorator;
import com.aa.mtg.MtgApplication;
import com.aa.mtg.game.init.test.InitTestService;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static application.browser.BattlefieldHelper.FIRST_LINE;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.sets.Ixalan.HEADWATER_SENTRIES;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.AZORIUS_GUILDGATE;
import static com.aa.mtg.game.player.PlayerType.PLAYER;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MtgApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({CastCreatureAlternativeCostTest.InitGameTestConfiguration.class})
public class CastCreatureAlternativeCostTest extends AbstractApplicationTest {

    @Autowired
    private InitTestServiceDecorator initTestServiceDecorator;

    public void setupGame() {
        initTestServiceDecorator.setInitTestService(new CastCreatureAlternativeCostTest.InitTestServiceForTest());
    }

    @Test
    @Ignore
    public void castCreatureAlternativeCost() {
        // When click on creature without paying the cost
        browser.player1().getHandHelper(PLAYER).getFirstCard(HEADWATER_SENTRIES).click();

        // Then stack is still empty
        browser.player1().getStackHelper().toHaveSize(0);

        // When clicking all lands
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(AZORIUS_GUILDGATE, 0).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 1).click();
        browser.player1().getBattlefieldHelper(PLAYER, FIRST_LINE).getCard(MOUNTAIN, 0).click();
    }

    static class InitTestServiceForTest extends InitTestService {
        @Override
        public void initGameStatus(GameStatus gameStatus) {
            addCardToCurrentPlayerHand(gameStatus, HEADWATER_SENTRIES);
            addCardToCurrentPlayerBattlefield(gameStatus, AZORIUS_GUILDGATE);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
            addCardToCurrentPlayerBattlefield(gameStatus, MOUNTAIN);
        }
    }
}
