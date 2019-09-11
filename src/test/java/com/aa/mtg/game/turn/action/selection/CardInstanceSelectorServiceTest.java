package com.aa.mtg.game.turn.action.selection;

import com.aa.TestUtils;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CardInstanceFactory;
import com.aa.mtg.cards.selector.CardInstanceSelector;
import com.aa.mtg.cards.selector.PowerToughnessConstraint;
import com.aa.mtg.game.status.GameStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.aa.mtg.cards.Cards.PLAINS;
import static com.aa.mtg.cards.properties.Type.CREATURE;
import static com.aa.mtg.cards.properties.Type.LAND;
import static com.aa.mtg.cards.selector.PowerToughnessConstraint.PowerOrToughness.POWER;
import static com.aa.mtg.cards.selector.PowerToughnessConstraint.PowerOrToughness.TOUGHNESS;
import static com.aa.mtg.cards.selector.PowerToughnessConstraintType.EQUAL;
import static com.aa.mtg.cards.selector.PowerToughnessConstraintType.GREATER;
import static com.aa.mtg.cards.selector.PowerToughnessConstraintType.LESS_OR_EQUAL;
import static com.aa.mtg.cards.selector.SelectorType.PERMANENT;
import static com.aa.mtg.cards.selector.StatusType.ATTACKING;
import static com.aa.mtg.cards.selector.StatusType.BLOCKING;
import static com.aa.mtg.cards.sets.Ixalan.FRENZIED_RAPTOR;
import static com.aa.mtg.cards.sets.Ixalan.GRAZING_WHIPTAIL;
import static com.aa.mtg.game.player.PlayerType.OPPONENT;
import static com.aa.mtg.game.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SelectionTestConfiguration.class)
public class CardInstanceSelectorServiceTest {

    @Autowired
    private CardInstanceSelectorService selectorService;

    @Autowired
    private CardInstanceFactory cardInstanceFactory;
    
    @Autowired
    private TestUtils testUtils;

    @Test
    public void selectionOnTargetPermanentFails() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .build();

        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionOnTargetPermanentPasses() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionOnTargetCreatureFails() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, PLAINS, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionOnTargetCreaturePasses() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionOnEqualToughnessFails() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(POWER, EQUAL, 2))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionOnEqualToughnessPasses() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(POWER, EQUAL, 3))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionOnGreaterPowerFails() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(POWER, GREATER, 4))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionOnGreaterPowerPasses() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(POWER, GREATER, 2))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionOnLessOrEqualToughnessFailsOnLess() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 3))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionOnLessOrEqualToughnessPassesOnEqual() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 4))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionOnLessOrEqualToughnessPassesOnGreater() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 5))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionPlayerCreatureCorrect() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .controllerType(PLAYER)
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionPlayerCreatureException() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .controllerType(PLAYER)
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        cardInstance.setController("opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionOpponentCreatureCorrect() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .controllerType(OPPONENT)
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "opponent-name");
        cardInstance.setController("opponent-name");
        gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionOpponentCreatureException() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .controllerType(OPPONENT)
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionAttackingCreature() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(singletonList(ATTACKING))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        cardInstance.getModifiers().setAttacking(true);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionAttackingCreatureException() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(singletonList(ATTACKING))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionBlockingCreature() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(singletonList(BLOCKING))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        cardInstance.getModifiers().setBlockingCardId(2);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionBlockingCreatureException() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(singletonList(BLOCKING))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionAttackingOrBlockingAttackingCreature() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        cardInstance.getModifiers().setAttacking(true);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionAttackingOrBlockingBlockingCreature() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        cardInstance.getModifiers().setBlockingCardId(2);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionAttackingOrBlockingException() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
                .selectorType(PERMANENT)
                .ofType(singletonList(CREATURE))
                .statusTypes(asList(ATTACKING, BLOCKING))
                .build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionAnother() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().others(true).selectorType(PERMANENT).ofType(singletonList(CREATURE)).build();
        CardInstance cardInstance1 = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        CardInstance cardInstance2 = cardInstanceFactory.create(gameStatus, 2, FRENZIED_RAPTOR, "player-name");
        cardInstance1.setController("player-name");
        cardInstance2.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance1);
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance2);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance1, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance2);
    }

    @Test
    public void selectionAnotherException() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().others(true).selectorType(PERMANENT).ofType(singletonList(CREATURE)).build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void selectionNonLand() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, GRAZING_WHIPTAIL, "player-name");
        cardInstance.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).containsExactly(cardInstance);
    }

    @Test
    public void selectionNonLandException() {
        // Given
        GameStatus gameStatus = testUtils.testGameStatus();

        CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).build();
        CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, PLAINS, "player-name");
        cardInstance.setController("player-name");
        gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

        // When
        List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

        // Then
        assertThat(selection).isEmpty();
    }

    @Test
    public void creaturesYouControlText() {
        assertThat(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).build().getText()).isEqualTo("Creatures you control");
    }

    @Test
    public void otherCreaturesYouControlText() {
        assertThat(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).controllerType(PLAYER).others(true).build().getText()).isEqualTo("Other creatures you control");
    }

    @Test
    public void allOtherCreaturesText() {
        assertThat(CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).others(true).build().getText()).isEqualTo("Other creatures");
    }
}