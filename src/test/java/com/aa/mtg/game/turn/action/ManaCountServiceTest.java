package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import integration.utils.TestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.aa.mtg.cards.Cards.ISLAND;
import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.properties.Cost.BLUE;
import static com.aa.mtg.cards.properties.Cost.WHITE;
import static com.aa.mtg.cards.sets.CoreSet2020.DARK_REMEDY;
import static org.assertj.core.api.Assertions.assertThat;

public class ManaCountServiceTest {

    private final ManaCountService manaCountService = new ManaCountService();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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

    @Test
    public void countManaPaidTappingInstant() {
        // Given
        Map<Integer, List<String>> mana = ImmutableMap.of(
                1, ImmutableList.of("WHITE")
        );
        GameStatus gameStatus = TestUtils.testGameStatus();
        Player player = gameStatus.getPlayer1();

        player.getBattlefield().addCard(new CardInstance(gameStatus, 1, DARK_REMEDY, player.getName(), player.getName()));

        thrown.expectMessage("The card you are trying to tap for mana is not a land.");

        // When
        manaCountService.verifyManaPaid(mana, player);
    }

    @Test
    public void countManaPaidTappingAlreadyTappedLand() {
        // Given
        Map<Integer, List<String>> mana = ImmutableMap.of(
                1, ImmutableList.of("WHITE")
        );
        GameStatus gameStatus = TestUtils.testGameStatus();
        Player player = gameStatus.getPlayer1();

        CardInstance plains = new CardInstance(gameStatus, 1, PLAINS, player.getName(), player.getName());
        plains.getModifiers().tap();
        player.getBattlefield().addCard(plains);

        thrown.expectMessage("The land you are trying to tap is already tapped.");

        // When
        manaCountService.verifyManaPaid(mana, player);
    }

    @Test
    public void countManaPaidTappingLandForWrongColor() {
        // Given
        Map<Integer, List<String>> mana = ImmutableMap.of(
                1, ImmutableList.of("BLUE")
        );
        GameStatus gameStatus = TestUtils.testGameStatus();
        Player player = gameStatus.getPlayer1();

        player.getBattlefield().addCard(new CardInstance(gameStatus, 1, PLAINS, player.getName(), player.getName()));

        thrown.expectMessage("\"1 - Plains\" cannot produce BLUE");

        // When
        manaCountService.verifyManaPaid(mana, player);
    }
}