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

import static com.aa.mtg.cards.ability.Abilities.LOSE_4_LIFE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LifeTestConfiguration.class)
public class EachPlayersAddXLifeActionTest {

    @Autowired
    private EachPlayersAddXLifeAction eachPlayersAddXLifeAction;

    @Test
    public void subtractXLifeToEachPlayer() {
        // Given
        GameStatus gameStatus = TestUtils.testGameStatus();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, CoreSet2020.DARK_REMEDY, "player-name");

        // When
        eachPlayersAddXLifeAction.perform(cardInstance, gameStatus, LOSE_4_LIFE);

        // Then
        assertThat(gameStatus.getPlayerByName("player-name").getLife()).isEqualTo(16);
        assertThat(gameStatus.getPlayerByName("opponent-name").getLife()).isEqualTo(16);
    }
}