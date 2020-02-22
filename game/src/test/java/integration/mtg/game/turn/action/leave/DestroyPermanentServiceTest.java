package integration.mtg.game.turn.action.leave;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.CardInstanceFactory;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.leave.DestroyPermanentService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LeaveTestConfiguration.class)
public class DestroyPermanentServiceTest {

    @Autowired
    private DestroyPermanentService destroyPermanentService;
    
    @Autowired
    private CardInstanceFactory cardInstanceFactory;

    @Autowired
    private TestUtils testUtils;

    @Autowired
    private Cards cards;

    @Test
    public void testDestroy() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
        gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

        // When
        destroyPermanentService.destroy(gameStatus, 61);

        // Then
        assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0);
        assertThat(gameStatus.getPlayer1().getGraveyard().getCards()).contains(cardInstance);
    }
}