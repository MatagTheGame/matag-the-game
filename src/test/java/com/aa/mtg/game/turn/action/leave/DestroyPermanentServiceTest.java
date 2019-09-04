package com.aa.mtg.game.turn.action.leave;

import com.aa.TestUtils;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.sets.CoreSet2020;
import com.aa.mtg.game.status.GameStatus;
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

    @Test
    public void testDestroy() {
        // Given
        GameStatus gameStatus = TestUtils.testGameStatus();
        CardInstance cardInstance = new CardInstance(gameStatus, 61, CoreSet2020.CANOPY_SPIDER, "player-name", "player-name");
        gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

        // When
        destroyPermanentService.destroy(gameStatus, 61);

        // Then
        assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0);
        assertThat(gameStatus.getPlayer1().getGraveyard().getCards()).contains(cardInstance);
    }
}