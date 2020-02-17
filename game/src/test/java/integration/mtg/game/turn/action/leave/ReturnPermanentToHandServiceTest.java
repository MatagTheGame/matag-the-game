package integration.mtg.game.turn.action.leave;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.CardInstanceFactory;
import com.aa.mtg.cards.sets.CoreSet2020;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.leave.ReturnPermanentToHandService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LeaveTestConfiguration.class)
public class ReturnPermanentToHandServiceTest {

    @Autowired
    private ReturnPermanentToHandService returnPermanentToHandService;

    @Autowired
    private CardInstanceFactory cardInstanceFactory;

    @Autowired
    private TestUtils testUtils;

    @Test
    public void testReturnToHand() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, CoreSet2020.CANOPY_SPIDER, "player-name", "player-name");
        gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

        // When
        returnPermanentToHandService.returnPermanentToHand(gameStatus, 61);

        // Then
        assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0);
        assertThat(gameStatus.getPlayer1().getHand().getCards()).contains(cardInstance);
    }
}