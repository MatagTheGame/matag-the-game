package integration.mtg.game.turn.action.draw;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.CardInstanceFactory;
import com.aa.mtg.cards.sets.CoreSet2020;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.draw.DrawXCardsAction;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.aa.mtg.cards.ability.Abilities.DRAW_1_CARD;
import static com.aa.mtg.cards.ability.Abilities.DRAW_2_CARDS;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = DrawTestConfiguration.class)
public class DrawXCardsActionTest {

    @Autowired
    private DrawXCardsAction drawXCardsAction;
    
    @Autowired
    private CardInstanceFactory cardInstanceFactory;

    @Autowired
    private TestUtils testUtils;

    @Test
    public void controllerDraw3Cards() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 12, CoreSet2020.DARK_REMEDY, "opponent-name");

        // When
        drawXCardsAction.perform(cardInstance, gameStatus, DRAW_2_CARDS);

        // Then
        assertThat(gameStatus.getPlayerByName("opponent-name").getHand().size()).isEqualTo(9);
        assertThat(gameStatus.getPlayerByName("opponent-name").getLibrary().size()).isEqualTo(31);
    }

    @Test
    public void currentPlayerDraw1Card() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        // When
        drawXCardsAction.perform(null, gameStatus, DRAW_1_CARD);

        // Then
        assertThat(gameStatus.getPlayerByName("player-name").getHand().size()).isEqualTo(8);
        assertThat(gameStatus.getPlayerByName("player-name").getLibrary().size()).isEqualTo(32);
    }
}