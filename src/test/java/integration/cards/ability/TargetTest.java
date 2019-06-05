package integration.cards.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;

import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint.PowerOrToughness.POWER;
import static com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint.PowerOrToughness.TOUGHNESS;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.EQUAL;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.GREATER;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.LESS_OR_EQUAL;
import static com.aa.mtg.cards.ability.target.TargetType.PERMANENT;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static integration.utils.TestUtils.testGameStatus;
import static java.util.Collections.singletonList;

public class TargetTest {
    @Test(expected = MessageException.class)
    public void selectionOnTargetPermanentFails() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .build();

        // When
        target.check(gameStatus, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnTargetPermanentPasses() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        target.check(gameStatus, 1);

        // Then NO exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOnTargetCreatureFails() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .build();
        CardInstance cardInstance = new CardInstance(1, PLAINS, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        target.check(gameStatus, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnTargetCreaturePasses() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        target.check(gameStatus, 1);

        // Then NO exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOnEqualToughnessFails() {
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
        target.check(gameStatus, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnEqualToughnessPasses() {
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
        target.check(gameStatus, 1);

        // Then NO exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOnGreaterPowerFails() {
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
        target.check(gameStatus, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnGreaterPowerPasses() {
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
        target.check(gameStatus, 1);

        // Then NO exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOnLessOrEqualToughnessFailsOnLess() {
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
        target.check(gameStatus, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnLessOrEqualToughnessPassesOnEqual() {
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
        target.check(gameStatus, 1);

        // Then no exception is thrown
    }

    @Test
    public void selectionOnLessOrEqualToughnessPassesOnGreater() {
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
        target.check(gameStatus, 1);

        // Then no exception is thrown
    }

    @Test
    public void selectionPlayerCreatureCorrect() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetControllerType(PLAYER)
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        target.check(gameStatus, 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionPlayerCreatureException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetControllerType(PLAYER)
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        target.check(gameStatus, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOpponentCreatureCorrect() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetControllerType(OPPONENT)
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        target.check(gameStatus, 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOpponentCreatureException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetControllerType(OPPONENT)
                .build();
        CardInstance cardInstance = new CardInstance(1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        target.check(gameStatus, 1);

        // Then an exception is thrown
    }
}