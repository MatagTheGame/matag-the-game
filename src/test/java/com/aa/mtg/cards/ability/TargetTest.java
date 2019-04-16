package com.aa.mtg.cards.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;

import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint.PowerOrToughness.POWER;
import static com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint.PowerOrToughness.TOUGHNESS;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.EQUAL;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.GREATER;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.LESS_OR_EQUAL;
import static com.aa.mtg.cards.ability.target.TargetType.PERMANENT;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.utils.TestUtils.testGameStatus;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

public class TargetTest {

    @Test
    public void selectionOnTargetPermanentReturnsFalse() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .build();

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void selectionOnTargetPermanentReturnsTrue() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void selectionOnTargetCreatureReturnsFalse() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .build();
        CardInstance cardInstance = new CardInstance(1, PLAINS, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void selectionOnTargetCreatureReturnsTrue() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void selectionOnEqualToughnessReturnsFalse() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(POWER, EQUAL, 2))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void selectionOnEqualToughnessReturnsTrue() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(POWER, EQUAL, 3))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void selectionOnGreaterPowerReturnsFalse() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(POWER, GREATER, 4))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void selectionOnGreaterPowerReturnsTrue() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(POWER, GREATER, 2))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void selectionOnLessOrEqualToughnessReturnsFalseOnLess() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 3))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isFalse();
    }

    @Test
    public void selectionOnLessOrEqualToughnessReturnsTrueOnEqual() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 4))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isTrue();
    }

    @Test
    public void selectionOnLessOrEqualToughnessReturnsTrueOnGreater() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetPowerToughnessConstraint(new TargetPowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 5))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        boolean valid = target.check(gameStatus);

        // Then
        assertThat(valid).isTrue();
    }
}