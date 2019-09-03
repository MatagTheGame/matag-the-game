package integration.game;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.selector.SelectorType;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.selector.PowerToughnessConstraint;
import com.aa.mtg.cards.selector.CardInstanceSelector;
import com.aa.mtg.game.message.MessageException;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.service.TargetCheckerService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.selector.PowerToughnessConstraint.PowerOrToughness.POWER;
import static com.aa.mtg.cards.selector.PowerToughnessConstraint.PowerOrToughness.TOUGHNESS;
import static com.aa.mtg.cards.selector.PowerToughnessConstraintType.EQUAL;
import static com.aa.mtg.cards.selector.PowerToughnessConstraintType.GREATER;
import static com.aa.mtg.cards.selector.PowerToughnessConstraintType.LESS_OR_EQUAL;
import static com.aa.mtg.cards.selector.StatusType.ATTACKING;
import static com.aa.mtg.cards.selector.StatusType.BLOCKING;
import static com.aa.mtg.cards.selector.SelectorType.PERMANENT;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.LAND;
import static com.aa.mtg.cards.sets.Ixalan.FRENZIED_RAPTOR;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static integration.TestUtils.testGameStatus;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TargetCheckerTest.TargetTestConfiguration.class)
public class TargetCheckerTest {
    
    @Autowired
    private TargetCheckerService targetCheckerService;
    
    @Test(expected = MessageException.class)
    public void selectionOnTargetPermanentFails() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .build();

        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 2);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnTargetPermanentPasses() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then NO exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOnTargetCreatureFails() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, PLAINS, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnTargetCreaturePasses() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then NO exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOnEqualToughnessFails() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(POWER, EQUAL, 2))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnEqualToughnessPasses() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(POWER, EQUAL, 3))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then NO exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOnGreaterPowerFails() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(POWER, GREATER, 4))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnGreaterPowerPasses() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(POWER, GREATER, 2))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then NO exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOnLessOrEqualToughnessFailsOnLess() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 3))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOnLessOrEqualToughnessPassesOnEqual() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 4))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test
    public void selectionOnLessOrEqualToughnessPassesOnGreater() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 5))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test
    public void selectionPlayerCreatureCorrect() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .controllerType(PLAYER)
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionPlayerCreatureException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .controllerType(PLAYER)
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionOpponentCreatureCorrect() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .controllerType(OPPONENT)
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getNonCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getNonCurrentPlayer().getName());
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionOpponentCreatureException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .controllerType(OPPONENT)
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionAttackingCreature() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(singletonList(ATTACKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance.getModifiers().setAttacking(true);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionAttackingCreatureException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(singletonList(ATTACKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionBlockingCreature() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(singletonList(BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance.getModifiers().setBlockingCardId(2);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionBlockingCreatureException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(singletonList(BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionAttackingOrBlockingAttackingCreature() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance.getModifiers().setAttacking(true);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test
    public void selectionAttackingOrBlockingBlockingCreature() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance.getModifiers().setBlockingCardId(2);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionAttackingOrBlockingException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionAnother() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().another(true).selectorType(PERMANENT).ofType(singletonList(CREATURE)).build();
        CardInstance cardInstance1 = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        CardInstance cardInstance2 = new CardInstance(gameStatus, 2, FRENZIED_RAPTOR, gameStatus.getCurrentPlayer().getName());
        cardInstance1.setController(gameStatus.getCurrentPlayer().getName());
        cardInstance2.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance1);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance2);

        // When
        targetCheckerService.check(gameStatus, cardInstance1, new Target(cardInstanceSelector), 2);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionAnotherException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().another(true).selectorType(PERMANENT).ofType(singletonList(CREATURE)).build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void selectionNonLand() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, GRAZING_WHIPTAIL, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void selectionNonLandException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, PLAINS, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void targetPlayer() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).build();

        // When
        targetCheckerService.check(gameStatus, null, new Target(cardInstanceSelector), "player-name");

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void targetPlayerException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).build();

        // When
        targetCheckerService.check(gameStatus, null, new Target(cardInstanceSelector), 1);

        // Then an exception is thrown
    }

    @Test
    public void targetOpponent() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).controllerType(OPPONENT).build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, PLAINS, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), "opponent-name");

        // Then no exception is thrown
    }

    @Test(expected = MessageException.class)
    public void targetOpponentException() {
        // Given
        GameStatus gameStatus = testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).controllerType(OPPONENT).build();
        CardInstance cardInstance = new CardInstance(gameStatus, 1, PLAINS, gameStatus.getCurrentPlayer().getName());
        cardInstance.setController(gameStatus.getCurrentPlayer().getName());
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        targetCheckerService.check(gameStatus, cardInstance, new Target(cardInstanceSelector), "player-name");

        // Then an exception is thrown
    }
    
    @Configuration
    @ComponentScan(basePackages = "com.aa.mtg")
    public static class TargetTestConfiguration {
        
    }
}