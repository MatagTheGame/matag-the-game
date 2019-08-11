package integration.cards.ability;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.TargetCheckerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint.PowerOrToughness.POWER;
import static com.aa.mtg.cards.ability.target.TargetPowerToughnessConstraint.PowerOrToughness.TOUGHNESS;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.EQUAL;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.GREATER;
import static com.aa.mtg.cards.ability.target.TargetSelectionConstraint.LESS_OR_EQUAL;
import static com.aa.mtg.cards.ability.target.TargetStatusType.ATTACKING;
import static com.aa.mtg.cards.ability.target.TargetStatusType.BLOCKING;
import static com.aa.mtg.cards.ability.target.TargetType.PERMANENT;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.LAND;
import static com.aa.mtg.cards.sets.Ixalan.FRENZIED_RAPTOR;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static integration.utils.TestUtils.testGameStatus;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TargetTest.TargetTestConfiguration.class)
public class TargetTest {
    
    @Autowired
    private TargetCheckerService targetCheckerService;
    
    @Test(expected = MessageException.class)
    public void selectionOnTargetPermanentFails() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .build();

        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 2);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnTargetPermanentPasses() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, PLAINS, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

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
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionAttackingCreature() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetStatusTypes(singletonList(ATTACKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance.getModifiers().setAttacking(true);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionAttackingCreatureException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetStatusTypes(singletonList(ATTACKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionBlockingCreature() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetStatusTypes(singletonList(BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance.getModifiers().setBlockingCardId(2);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionBlockingCreatureException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetStatusTypes(singletonList(BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionAttackingOrBlockingAttackingCreature() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetStatusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance.getModifiers().setAttacking(true);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then no exception is thrown
    }

    @Test
    public void selectionAttackingOrBlockingBlockingCreature() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetStatusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance.getModifiers().setBlockingCardId(2);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionAttackingOrBlockingException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder()
                .targetType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .targetStatusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionAnother() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder().another(true).targetType(PERMANENT).ofType(singletonList(CREATURE)).build();
        CardInstance cardInstance1 = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        CardInstance cardInstance2 = new CardInstance(gameStatus, 2, FRENZIED_RAPTOR, gameStatus.getCurrentPlayer().getName());
        cardInstance1.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance2.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance1);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance2);

        // When
        targetCheckerService.check(gameStatus, cardInstance1, target, 2);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionAnotherException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder().another(true).targetType(PERMANENT).ofType(singletonList(CREATURE)).build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionNonLand() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder().targetType(PERMANENT).notOfType(singletonList(LAND)).build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionNonLandException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        Target target = Target.builder().targetType(PERMANENT).notOfType(singletonList(LAND)).build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, PLAINS, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, target, 1);

        // Then an exception is thrown
    }
    
    @Configuration
    @ComponentScan(basePackages = "com.aa.mtg")
    public static class TargetTestConfiguration {
        
    }
}