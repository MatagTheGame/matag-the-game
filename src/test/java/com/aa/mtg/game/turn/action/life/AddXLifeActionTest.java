package com.aa.mtg.game.turn.action.life;

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
@ContextConfiguration(classes = LifeTestConfiguration.class)
public class AddXLifeActionTest {

    @Autowired
    private AddXLifeAction addXLifeAction;

    @Test
    public void addLifeToPlayer() {
        // Given
        GameStatus gameStatus = TestUtils.testGameStatus();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, CoreSet2020.DARK_REMEDY, "player-name", "player-name");
        String parameter = "3";

        // When
        addXLifeAction.perform(cardInstance, gameStatus, parameter);

        // Then
        assertThat(gameStatus.getPlayerByName("player-name").getLife()).isEqualTo(23);
    }
}