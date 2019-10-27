package integration.mtg.cards;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CardInstanceFactory;
import com.aa.mtg.cards.sets.Dominaria;
import com.aa.mtg.game.status.GameStatus;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestUtilsConfiguration.class)
public class CardInstanceFactoryTest {

    @Autowired
    private CardInstanceFactory cardInstanceFactory;

    @Autowired
    private TestUtils testUtils;

    @Test
    public void shouldCreateACardInstance() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        // When
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, Dominaria.SHORT_SWORD, "player-name");

        // Then
        assertThat(cardInstance.getId()).isEqualTo(1);
        assertThat(cardInstance.getOwner()).isEqualTo("player-name");
    }

    @Test
    public void shouldCreateTwoDifferentCardInstances() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        // When
        CardInstance cardInstance1 = cardInstanceFactory.create(gameStatus, 1, Dominaria.SHORT_SWORD, "player-name");
        CardInstance cardInstance2 = cardInstanceFactory.create(gameStatus, 2, Dominaria.BEFUDDLE, "opponent-name");

        // Then
        assertThat(cardInstance1).isNotSameAs(cardInstance2);
    }

}