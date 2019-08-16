package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import integration.utils.TestUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.properties.Cost.BLUE;
import static com.aa.mtg.cards.properties.Cost.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

public class ManaCountServiceTest {

    private final ManaCountService manaCountService = new ManaCountService();

    @Test
    public void countManaPaidForSimpleLands() {
        // Given
        Map<Integer, List<String>> mana = ImmutableMap.of(
                1, ImmutableList.of("WHITE"),
                2, ImmutableList.of("WHITE"),
                3, ImmutableList.of("BLUE")
        );
        GameStatus gameStatus = TestUtils.testGameStatus();
        Player player = gameStatus.getPlayer1();

        player.getBattlefield().addCard(new CardInstance(gameStatus, 1, PLAINS, player.getName(), player.getName()));
        player.getBattlefield().addCard(new CardInstance(gameStatus, 2, PLAINS, player.getName(), player.getName()));
        player.getBattlefield().addCard(new CardInstance(gameStatus, 3, ISLAND, player.getName(), player.getName()));

        // When
        ArrayList<Cost> colors = manaCountService.verifyManaPaid(mana, player);

        // Then
        assertThat(colors).isEqualTo(ImmutableList.of(WHITE, WHITE, BLUE));
    }
}