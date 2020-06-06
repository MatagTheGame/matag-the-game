package integration.turn.action.selection;

import com.matag.cards.Cards;
import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.cards.ability.selector.PowerToughnessConstraint;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.player.Player;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.CardInstanceSelectorService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static com.matag.cards.ability.selector.PowerToughnessConstraint.PowerOrToughness.POWER;
import static com.matag.cards.ability.selector.PowerToughnessConstraint.PowerOrToughness.TOUGHNESS;
import static com.matag.cards.ability.selector.PowerToughnessConstraintType.*;
import static com.matag.cards.ability.selector.SelectorType.PERMANENT;
import static com.matag.cards.ability.selector.SelectorType.SPELL;
import static com.matag.cards.ability.selector.StatusType.ATTACKING;
import static com.matag.cards.ability.selector.StatusType.BLOCKING;
import static com.matag.cards.ability.selector.TurnStatusType.YOUR_TURN;
import static com.matag.cards.ability.type.AbilityType.FLYING;
import static com.matag.cards.properties.Color.GREEN;
import static com.matag.cards.properties.Color.WHITE;
import static com.matag.cards.properties.Subtype.ZOMBIE;
import static com.matag.cards.properties.Type.*;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
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

  @Autowired
  private Cards cards;

  @Test
  public void selectionOnTargetPermanentFails() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
      .selectorType(PERMANENT)
      .build();

    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");

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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance1 = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    CardInstance cardInstance2 = cardInstanceFactory.create(gameStatus, 2, cards.get("Frenzied Raptor"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
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
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), "player-name");
    cardInstance.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionZombiesYouControl() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).ofSubtype(singletonList(ZOMBIE)).others(true).build();
    CardInstance otherZombiesCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Death Baron"), "player-name");
    otherZombiesCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(otherZombiesCreature);

    CardInstance aZombie = cardInstanceFactory.create(gameStatus, 2, cards.get("Diregraf Ghoul"), "player-name");
    aZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aZombie);

    CardInstance aNonZombie = cardInstanceFactory.create(gameStatus, 3, cards.get("Daybreak Chaplain"), "player-name");
    aNonZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aNonZombie);

    CardInstance anOpponentZombie = cardInstanceFactory.create(gameStatus, 4, cards.get("Daybreak Chaplain"), "player-name");
    anOpponentZombie.setController("opponent-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anOpponentZombie);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, otherZombiesCreature, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aZombie);
  }

  @Test
  public void selectionFliersYouControl() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).others(true).ofType(singletonList(CREATURE)).withAbilityType(FLYING).controllerType(PLAYER).build();
    CardInstance otherFliersCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    otherFliersCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(otherFliersCreature);

    CardInstance aFlier = cardInstanceFactory.create(gameStatus, 2, cards.get("Dawning Angel"), "player-name");
    aFlier.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aFlier);

    CardInstance aNonFlier = cardInstanceFactory.create(gameStatus, 3, cards.get("Daybreak Chaplain"), "player-name");
    aNonFlier.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aNonFlier);

    CardInstance anOpponentFlier = cardInstanceFactory.create(gameStatus, 4, cards.get("Dawning Angel"), "player-name");
    anOpponentFlier.setController("opponent-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anOpponentFlier);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, otherFliersCreature, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aFlier);
  }

  @Test
  public void selectionCreatureOrPlaneswalkerGreenOrWhite() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();


    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).ofType(asList(CREATURE, PLANESWALKER)).ofColors(asList(GREEN, WHITE)).build();

    CardInstance whiteCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(whiteCreature);

    CardInstance blackCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Barony Vampire"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(blackCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(whiteCreature);
  }

  @Test
  public void selectionItself() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).itself(true).build();

    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aPermanent, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectionItselfAsLongItsYourTurnYes() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).itself(true).turnStatusType(YOUR_TURN).build();

    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    aPermanent.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aPermanent, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectionItselfAsLongItsYourTurnNo() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).itself(true).turnStatusType(YOUR_TURN).build();

    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "opponent-name");
    aPermanent.setController("opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aPermanent, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionCurrentEnchanted() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(PERMANENT).currentEnchanted(true).build();

    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    aPermanent.setController("player-name");
    CardInstance theEnchantment = cardInstanceFactory.create(gameStatus, 2, cards.get("Colossification"), "player-name");
    theEnchantment.setController("player-name");
    theEnchantment.getModifiers().setAttachedToId(aPermanent.getId());

    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(theEnchantment);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, theEnchantment, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectPlayer() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).itself(true).build();
    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name");

    // When
    List<Player> selection = selectorService.selectPlayers(gameStatus, aPermanent, cardInstanceSelector);

    // Then
    assertThat(selection).containsExactly(gameStatus.getPlayer1());
  }

  @Test
  public void selectOpponent() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).controllerType(OPPONENT).build();
    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name");

    // When
    List<Player> selection = selectorService.selectPlayers(gameStatus, aPermanent, cardInstanceSelector);

    // Then
    assertThat(selection).containsExactly(gameStatus.getPlayer2());
  }

  @Test
  public void selectAllPlayers() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder().selectorType(SelectorType.PLAYER).controllerType(PLAYER).build();
    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name");

    // When
    List<Player> selection = selectorService.selectPlayers(gameStatus, aPermanent, cardInstanceSelector);

    // Then
    assertThat(selection).containsExactlyInAnyOrder(gameStatus.getPlayer1(), gameStatus.getPlayer2());
  }

  @Test
  public void selectionSpellEmpty() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
      .selectorType(SPELL)
      .build();

    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionAnySpell() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
      .selectorType(SPELL)
      .build();

    CardInstance aSpell = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    gameStatus.getStack().add(aSpell);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aSpell, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aSpell);
  }

  @Test
  public void selectionAnInstantNotMatched() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
      .selectorType(SPELL)
      .ofType(List.of(INSTANT))
      .build();

    CardInstance aSpell = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    gameStatus.getStack().add(aSpell);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aSpell, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionAnInstantOfSorcerySpell() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    CardInstanceSelector cardInstanceSelector = CardInstanceSelector.builder()
      .selectorType(SPELL)
      .ofType(List.of(INSTANT, SORCERY))
      .build();

    CardInstance anInstant = cardInstanceFactory.create(gameStatus, 1, cards.get("Precision Bolt"), "player-name");
    gameStatus.getStack().add(anInstant);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, anInstant, cardInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(anInstant);
  }
}