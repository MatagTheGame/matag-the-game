package com.aa.mtg.game.turn.action.draw;

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
@ContextConfiguration(classes = DrawTestConfiguration.class)
public class DrawXCardsActionTest {

    @Autowired
    private DrawXCardsAction drawXCardsAction;

    @Test
    public void controllerDraw3Cards() {
        // Given
        GameStatus gameStatus = TestUtils.testGameStatus();
        CardInstance cardInstance = new CardInstance(gameStatus, 12, CoreSet2020.DARK_REMEDY, "opponent-name");
        String parameter = "3";

        // When
        drawXCardsAction.perform(cardInstance, gameStatus, parameter);

        // Then
        assertThat(gameStatus.getPlayerByName("opponent-name").getHand().size()).isEqualTo(10);
        assertThat(gameStatus.getPlayerByName("opponent-name").getLibrary().size()).isEqualTo(50);
    }

    @Test
    public void currentPlayerDraw1Cardd() {
        // Given
        GameStatus gameStatus = TestUtils.testGameStatus();
        String parameter = "1";

        // When
        drawXCardsAction.perform(null, gameStatus, parameter);

        // Then
        assertThat(gameStatus.getPlayerByName("player-name").getHand().size()).isEqualTo(8);
        assertThat(gameStatus.getPlayerByName("player-name").getLibrary().size()).isEqualTo(52);
    }
}