package integration.turn.action.selection;

import com.matag.cards.Cards;
import com.matag.cards.ability.selector.MagicInstanceSelector;
import com.matag.cards.ability.selector.PowerToughnessConstraint;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService;
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
import static com.matag.cards.ability.selector.StatusType.*;
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
public class MagicInstancePermanentSelectorServiceTest {

  @Autowired
  private MagicInstancePermanentSelectorService selectorService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Test
  public void emptyBattlefield() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).build();

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectAnyPermanent() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .build();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(cardInstance);
  }

  @Test
  public void selectCreature() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .build();

    CardInstance aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(aCreature);
    CardInstance aLand = cardInstanceFactory.create(gameStatus, 2, cards.get("Plains"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(aLand);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aCreature);
  }

  @Test
  public void selectionOnEqualPower() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .powerToughnessConstraint(new PowerToughnessConstraint(POWER, EQUAL, 3))
      .build();

    CardInstance creaturePower3 = cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower3);
    CardInstance creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower4);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creaturePower3);
  }

  @Test
  public void selectionOnGreaterPowerFails() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .powerToughnessConstraint(new PowerToughnessConstraint(POWER, GREATER, 3))
      .build();

    CardInstance creaturePower3 = cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower3);
    CardInstance creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower4);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creaturePower4);
  }

  @Test
  public void selectionOnLessOrEqualToughnessPassesOnEqual() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 3))
      .build();

    CardInstance creaturePower3 = cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower3);
    CardInstance creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower4);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creaturePower3);
  }

  @Test
  public void selectionPlayerCreature() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .controllerType(PLAYER)
      .build();

    CardInstance playerCard = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    playerCard.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(playerCard);

    CardInstance opponentCard = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "opponent-name");
    opponentCard.setController("opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(opponentCard);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).contains(playerCard);
  }

  @Test
  public void selectionColorlessCreature() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .colorless(true)
      .build();

    CardInstance redCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    redCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(redCreature);

    CardInstance colorlessCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Jousting Dummy"), "player-name");
    colorlessCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(colorlessCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(colorlessCreature);
  }

  @Test
  public void selectionMulticolorCreatureFails() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .multicolor(true)
      .build();

    CardInstance redCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    redCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(redCreature);

    CardInstance multicolorCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Adeliz, the Cinder Wind"), "player-name");
    multicolorCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(multicolorCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(multicolorCreature);
  }

  @Test
  public void selectionOpponentCreatureCorrect() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .controllerType(OPPONENT)
      .build();

    CardInstance playerCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    playerCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(playerCreature);

    CardInstance opponentCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "opponent-name");
    opponentCreature.setController("opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(opponentCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(opponentCreature);
  }

  @Test
  public void selectionAttackingCreature() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(singletonList(ATTACKING))
      .build();

    CardInstance attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    attackingCreature.setController("player-name");
    attackingCreature.getModifiers().setAttacking(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature);

    CardInstance nonAttackingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    nonAttackingCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(nonAttackingCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus,  null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(attackingCreature);
  }

  @Test
  public void selectionCreatureWhoAttacked() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(singletonList(ATTACKED))
      .build();

    CardInstance creatureWhoAttacked = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    creatureWhoAttacked.setController("player-name");
    creatureWhoAttacked.getModifiers().getModifiersUntilEndOfTurn().setAttacked(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(creatureWhoAttacked);

    CardInstance creatureWhoDidNotAttacked = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    creatureWhoDidNotAttacked.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(creatureWhoDidNotAttacked);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus,  null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creatureWhoAttacked);
  }

  @Test
  public void selectionBlockingCreature() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(singletonList(BLOCKING))
      .build();

    CardInstance blockingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    blockingCreature.setController("player-name");
    blockingCreature.getModifiers().setBlockingCardId(3);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(blockingCreature);

    CardInstance nonBlockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    nonBlockingCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(nonBlockingCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(blockingCreature);
  }

  @Test
  public void selectionCreatureWhoBlocked() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(singletonList(BLOCKED))
      .build();

    CardInstance creatureWhoBlocked = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    creatureWhoBlocked.setController("player-name");
    creatureWhoBlocked.getModifiers().setBlockingCardId(3);
    creatureWhoBlocked.getModifiers().getModifiersUntilEndOfTurn().setBlocked(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(creatureWhoBlocked);

    CardInstance creatureWhoDidNotBlocked = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    creatureWhoDidNotBlocked.setController("player-name");
    creatureWhoDidNotBlocked.getModifiers().setBlockingCardId(3);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(creatureWhoDidNotBlocked);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creatureWhoBlocked);
  }

  @Test
  public void selectionAttackingOrBlockingAttackingCreature() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(asList(ATTACKING, BLOCKING))
      .build();

    CardInstance attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    attackingCreature.setController("player-name");
    attackingCreature.getModifiers().setAttacking(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature);

    CardInstance blockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    blockingCreature.setController("player-name");
    blockingCreature.getModifiers().setBlockingCardId(4);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(blockingCreature);

    CardInstance otherCreature = cardInstanceFactory.create(gameStatus, 3, cards.get("Grazing Whiptail"), "player-name");
    otherCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(otherCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, otherCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(attackingCreature, blockingCreature);
  }

  @Test
  public void selectionTappedCreature() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .statusTypes(singletonList(TAPPED))
      .build();

    CardInstance tappedCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    tappedCreature.setController("player-name");
    tappedCreature.getModifiers().setTapped(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(tappedCreature);

    CardInstance untappedCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    untappedCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(untappedCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(tappedCreature);
  }

  @Test
  public void selectionAnother() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().others(true).selectorType(PERMANENT).ofType(singletonList(CREATURE)).build();
    CardInstance aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    aCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aCreature);

    CardInstance anotherCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Frenzied Raptor"), "player-name");
    anotherCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anotherCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(anotherCreature);
  }

  @Test
  public void selectionNonLand() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).build();

    CardInstance nonLand = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    nonLand.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(nonLand);

    CardInstance land = cardInstanceFactory.create(gameStatus, 2, cards.get("Plains"), "player-name");
    land.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(land);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(nonLand);
  }

  @Test
  public void selectionZombiesYouControl() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).ofSubtype(singletonList(ZOMBIE)).controllerType(PLAYER).others(true).build();
    CardInstance otherZombiesCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Death Baron"), "player-name");
    otherZombiesCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(otherZombiesCreature);

    CardInstance aZombie = cardInstanceFactory.create(gameStatus, 2, cards.get("Diregraf Ghoul"), "player-name");
    aZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aZombie);

    CardInstance aNonZombie = cardInstanceFactory.create(gameStatus, 3, cards.get("Daybreak Chaplain"), "player-name");
    aNonZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aNonZombie);

    CardInstance anOpponentZombie = cardInstanceFactory.create(gameStatus, 4, cards.get("Diregraf Ghoul"), "player-name");
    anOpponentZombie.setController("opponent-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anOpponentZombie);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aZombie, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(otherZombiesCreature);
  }

  @Test
  public void selectionNonZombies() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).notOfSubtype(singletonList(ZOMBIE)).build();
    CardInstance aZombie = cardInstanceFactory.create(gameStatus, 1, cards.get("Diregraf Ghoul"), "player-name");
    aZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aZombie);

    CardInstance aNonZombie = cardInstanceFactory.create(gameStatus, 2, cards.get("Daybreak Chaplain"), "player-name");
    aNonZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aNonZombie);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aNonZombie);
  }

  @Test
  public void selectionFliersYouControl() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).others(true).ofType(singletonList(CREATURE)).withAbilityType(FLYING).controllerType(PLAYER).build();
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
    List<CardInstance> selection = selectorService.select(gameStatus, otherFliersCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aFlier);
  }

  @Test
  public void selectionCreatureOrPlaneswalkerGreenOrWhite() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();


    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).ofType(asList(CREATURE, PLANESWALKER)).ofColors(asList(GREEN, WHITE)).build();

    CardInstance whiteCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(whiteCreature);

    CardInstance blackCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Barony Vampire"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(blackCreature);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(whiteCreature);
  }

  @Test
  public void selectionItself() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).itself(true).build();

    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectionItselfAsLongItsYourTurnYes() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).itself(true).turnStatusType(YOUR_TURN).build();

    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    aPermanent.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectionItselfAsLongItsYourTurnNo() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).itself(true).turnStatusType(YOUR_TURN).build();

    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "opponent-name");
    aPermanent.setController("opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionCurrentEnchanted() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).currentEnchanted(true).build();

    CardInstance aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    aPermanent.setController("player-name");
    CardInstance theEnchantment = cardInstanceFactory.create(gameStatus, 2, cards.get("Colossification"), "player-name");
    theEnchantment.setController("player-name");
    theEnchantment.getModifiers().setAttachedToId(aPermanent.getId());

    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(theEnchantment);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, theEnchantment, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectionHistoric() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).historic(true).build();

    CardInstance aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    aCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aCreature);

    CardInstance anArtifact = cardInstanceFactory.create(gameStatus, 2, cards.get("Jousting Dummy"), "player-name");
    anArtifact.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anArtifact);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(anArtifact);
  }

  @Test
  public void selectionSpellEmpty() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(SPELL)
      .build();

    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, cardInstance, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionAnySpell() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(SPELL)
      .build();

    CardInstance aSpell = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    gameStatus.getStack().add(aSpell);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aSpell, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aSpell);
  }

  @Test
  public void selectionAnInstantNotMatched() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(SPELL)
      .ofType(List.of(INSTANT))
      .build();

    CardInstance aSpell = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    gameStatus.getStack().add(aSpell);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, aSpell, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionAnInstantOfSorcerySpell() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    MagicInstanceSelector magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(SPELL)
      .ofType(List.of(INSTANT, SORCERY))
      .build();

    CardInstance anInstant = cardInstanceFactory.create(gameStatus, 1, cards.get("Precision Bolt"), "player-name");
    gameStatus.getStack().add(anInstant);

    // When
    List<CardInstance> selection = selectorService.select(gameStatus, anInstant, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(anInstant);
  }
}