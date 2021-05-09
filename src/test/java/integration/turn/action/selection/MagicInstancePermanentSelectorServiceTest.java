package integration.turn.action.selection;

import static com.matag.cards.ability.selector.PowerToughnessConstraint.PowerOrToughness.POWER;
import static com.matag.cards.ability.selector.PowerToughnessConstraint.PowerOrToughness.TOUGHNESS;
import static com.matag.cards.ability.selector.PowerToughnessConstraintType.EQUAL;
import static com.matag.cards.ability.selector.PowerToughnessConstraintType.GREATER;
import static com.matag.cards.ability.selector.PowerToughnessConstraintType.LESS_OR_EQUAL;
import static com.matag.cards.ability.selector.SelectorType.PERMANENT;
import static com.matag.cards.ability.selector.SelectorType.SPELL;
import static com.matag.cards.ability.selector.StatusType.ATTACKED;
import static com.matag.cards.ability.selector.StatusType.ATTACKING;
import static com.matag.cards.ability.selector.StatusType.BLOCKED;
import static com.matag.cards.ability.selector.StatusType.BLOCKING;
import static com.matag.cards.ability.selector.StatusType.TAPPED;
import static com.matag.cards.ability.selector.TurnStatusType.YOUR_TURN;
import static com.matag.cards.ability.type.AbilityType.FLYING;
import static com.matag.cards.properties.Color.GREEN;
import static com.matag.cards.properties.Color.WHITE;
import static com.matag.cards.properties.Subtype.ZOMBIE;
import static com.matag.cards.properties.Type.ARTIFACT;
import static com.matag.cards.properties.Type.CREATURE;
import static com.matag.cards.properties.Type.INSTANT;
import static com.matag.cards.properties.Type.LAND;
import static com.matag.cards.properties.Type.PLANESWALKER;
import static com.matag.cards.properties.Type.SORCERY;
import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.matag.cards.Cards;
import com.matag.cards.ability.selector.MagicInstanceSelector;
import com.matag.cards.ability.selector.PowerToughnessConstraint;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.turn.action.selection.MagicInstancePermanentSelectorService;

import integration.TestUtils;

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
    var gameStatus = testUtils.testGameStatus();
    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).build();

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectAnyPermanent() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .build();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(cardInstance);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(cardInstance);
  }

  @Test
  public void selectCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .build();

    var aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(aCreature);
    var aLand = cardInstanceFactory.create(gameStatus, 2, cards.get("Plains"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(aLand);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aCreature);
  }

  @Test
  public void selectionOnEqualPower() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .powerToughnessConstraint(new PowerToughnessConstraint(POWER, EQUAL, 3))
      .build();

    var creaturePower3 = cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower3);
    var creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower4);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creaturePower3);
  }

  @Test
  public void selectionOnGreaterPowerFails() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .powerToughnessConstraint(new PowerToughnessConstraint(POWER, GREATER, 3))
      .build();

    var creaturePower3 = cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower3);
    var creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower4);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creaturePower4);
  }

  @Test
  public void selectionOnLessOrEqualToughnessPassesOnEqual() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .powerToughnessConstraint(new PowerToughnessConstraint(TOUGHNESS, LESS_OR_EQUAL, 3))
      .build();

    var creaturePower3 = cardInstanceFactory.create(gameStatus, 1, cards.get("Angel of the Dawn"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower3);
    var creaturePower4 = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(creaturePower4);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creaturePower3);
  }

  @Test
  public void selectionPlayerCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .controllerType(PLAYER)
      .build();

    var playerCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    playerCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(playerCreature);

    var opponentCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "opponent-name");
    opponentCreature.setController("opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(opponentCreature);

    // When
    var selection = selectorService.select(gameStatus, playerCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).contains(playerCreature);
  }

  @Test
  public void selectionOpponentCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .controllerType(OPPONENT)
      .build();

    var playerCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    playerCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(playerCreature);

    var opponentCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "opponent-name");
    opponentCreature.setController("opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(opponentCreature);

    // When
    var selection = selectorService.select(gameStatus, playerCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(opponentCreature);
  }

  @Test
  public void selectionPlayerCreatureOnOpponentTurnUsingOwnershipWhenControllerIsMissing() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentTurnPlayer("opponent-name");

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .controllerType(OPPONENT)
      .build();

    var playerCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Battlefield Promotion"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(playerCreature);

    var opponentCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "opponent-name");
    opponentCreature.setController("opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(opponentCreature);

    // When
    var selection = selectorService.select(gameStatus, playerCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(opponentCreature);
  }

  @Test
  public void selectionColorlessCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .colorless(true)
      .build();

    var redCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    redCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(redCreature);

    var colorlessCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Jousting Dummy"), "player-name");
    colorlessCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(colorlessCreature);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(colorlessCreature);
  }

  @Test
  public void selectionMulticolorCreatureFails() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .multicolor(true)
      .build();

    var redCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    redCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(redCreature);

    var multicolorCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Adeliz, the Cinder Wind"), "player-name");
    multicolorCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(multicolorCreature);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(multicolorCreature);
  }

  @Test
  public void selectionAttackingCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(singletonList(ATTACKING))
      .build();

    var attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    attackingCreature.setController("player-name");
    attackingCreature.getModifiers().setAttacking(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature);

    var nonAttackingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    nonAttackingCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(nonAttackingCreature);

    // When
    var selection = selectorService.select(gameStatus,  null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(attackingCreature);
  }

  @Test
  public void selectionCreatureWhoAttacked() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(singletonList(ATTACKED))
      .build();

    var creatureWhoAttacked = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    creatureWhoAttacked.setController("player-name");
    creatureWhoAttacked.getModifiers().getModifiersUntilEndOfTurn().setAttacked(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(creatureWhoAttacked);

    var creatureWhoDidNotAttacked = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    creatureWhoDidNotAttacked.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(creatureWhoDidNotAttacked);

    // When
    var selection = selectorService.select(gameStatus,  null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creatureWhoAttacked);
  }

  @Test
  public void selectionBlockingCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(singletonList(BLOCKING))
      .build();

    var blockingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    blockingCreature.setController("player-name");
    blockingCreature.getModifiers().setBlockingCardId(3);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(blockingCreature);

    var nonBlockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    nonBlockingCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(nonBlockingCreature);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(blockingCreature);
  }

  @Test
  public void selectionCreatureWhoBlocked() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(singletonList(BLOCKED))
      .build();

    var creatureWhoBlocked = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    creatureWhoBlocked.setController("player-name");
    creatureWhoBlocked.getModifiers().setBlockingCardId(3);
    creatureWhoBlocked.getModifiers().getModifiersUntilEndOfTurn().setBlocked(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(creatureWhoBlocked);

    var creatureWhoDidNotBlocked = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    creatureWhoDidNotBlocked.setController("player-name");
    creatureWhoDidNotBlocked.getModifiers().setBlockingCardId(3);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(creatureWhoDidNotBlocked);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(creatureWhoBlocked);
  }

  @Test
  public void selectionAttackingOrBlockingAttackingCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofType(singletonList(CREATURE))
      .statusTypes(asList(ATTACKING, BLOCKING))
      .build();

    var attackingCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    attackingCreature.setController("player-name");
    attackingCreature.getModifiers().setAttacking(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(attackingCreature);

    var blockingCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    blockingCreature.setController("player-name");
    blockingCreature.getModifiers().setBlockingCardId(4);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(blockingCreature);

    var otherCreature = cardInstanceFactory.create(gameStatus, 3, cards.get("Grazing Whiptail"), "player-name");
    otherCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(otherCreature);

    // When
    var selection = selectorService.select(gameStatus, otherCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(attackingCreature, blockingCreature);
  }

  @Test
  public void selectionTappedCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .statusTypes(singletonList(TAPPED))
      .build();

    var tappedCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    tappedCreature.setController("player-name");
    tappedCreature.getModifiers().setTapped(true);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(tappedCreature);

    var untappedCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Grazing Whiptail"), "player-name");
    untappedCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(untappedCreature);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(tappedCreature);
  }

  @Test
  public void selectionAnother() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().others(true).selectorType(PERMANENT).ofType(singletonList(CREATURE)).build();
    var aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    aCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aCreature);

    var anotherCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Frenzied Raptor"), "player-name");
    anotherCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anotherCreature);

    // When
    var selection = selectorService.select(gameStatus, aCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(anotherCreature);
  }

  @Test
  public void selectionNonLand() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).notOfType(singletonList(LAND)).build();

    var nonLand = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    nonLand.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(nonLand);

    var land = cardInstanceFactory.create(gameStatus, 2, cards.get("Plains"), "player-name");
    land.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(land);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(nonLand);
  }

  @Test
  public void selectionZombiesYouControl() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).ofSubtype(singletonList(ZOMBIE)).controllerType(PLAYER).others(true).build();
    var otherZombiesCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Death Baron"), "player-name");
    otherZombiesCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(otherZombiesCreature);

    var aZombie = cardInstanceFactory.create(gameStatus, 2, cards.get("Diregraf Ghoul"), "player-name");
    aZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aZombie);

    var aNonZombie = cardInstanceFactory.create(gameStatus, 3, cards.get("Daybreak Chaplain"), "player-name");
    aNonZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aNonZombie);

    var anOpponentZombie = cardInstanceFactory.create(gameStatus, 4, cards.get("Diregraf Ghoul"), "player-name");
    anOpponentZombie.setController("opponent-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anOpponentZombie);

    // When
    var selection = selectorService.select(gameStatus, aZombie, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(otherZombiesCreature);
  }

  @Test
  public void selectionNonZombies() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).notOfSubtype(singletonList(ZOMBIE)).build();
    var aZombie = cardInstanceFactory.create(gameStatus, 1, cards.get("Diregraf Ghoul"), "player-name");
    aZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aZombie);

    var aNonZombie = cardInstanceFactory.create(gameStatus, 2, cards.get("Daybreak Chaplain"), "player-name");
    aNonZombie.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aNonZombie);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aNonZombie);
  }

  @Test
  public void selectionFliersYouControl() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).others(true).ofType(singletonList(CREATURE)).withAbilityType(FLYING).controllerType(PLAYER).build();
    var otherFliersCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    otherFliersCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(otherFliersCreature);

    var aFlier = cardInstanceFactory.create(gameStatus, 2, cards.get("Dawning Angel"), "player-name");
    aFlier.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aFlier);

    var aNonFlier = cardInstanceFactory.create(gameStatus, 3, cards.get("Daybreak Chaplain"), "player-name");
    aNonFlier.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aNonFlier);

    var anOpponentFlier = cardInstanceFactory.create(gameStatus, 4, cards.get("Dawning Angel"), "player-name");
    anOpponentFlier.setController("opponent-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anOpponentFlier);

    // When
    var selection = selectorService.select(gameStatus, otherFliersCreature, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aFlier);
  }

  @Test
  public void selectionNotFliersYouControl() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).ofType(singletonList(CREATURE)).withoutAbilityType(FLYING).controllerType(PLAYER).build();
    var aFlier = cardInstanceFactory.create(gameStatus, 1, cards.get("Dawning Angel"), "player-name");
    aFlier.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aFlier);

    var aNonFlier = cardInstanceFactory.create(gameStatus, 2, cards.get("Daybreak Chaplain"), "player-name");
    aNonFlier.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aNonFlier);

    var anOpponentFlier = cardInstanceFactory.create(gameStatus, 3, cards.get("Dawning Angel"), "player-name");
    anOpponentFlier.setController("opponent-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anOpponentFlier);

    // When
    var selection = selectorService.select(gameStatus, aFlier, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aNonFlier);
  }

  @Test
  public void selectionCreatureOrPlaneswalkerGreenOrWhite() {
    // Given
    var gameStatus = testUtils.testGameStatus();


    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).ofType(asList(CREATURE, PLANESWALKER)).ofColors(asList(GREEN, WHITE)).build();

    var whiteCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(whiteCreature);

    var blackCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Barony Vampire"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(blackCreature);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(whiteCreature);
  }

  @Test
  public void selectionItself() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).itself(true).build();

    var aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    var selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectionItselfAsLongItsYourTurnYes() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).itself(true).turnStatusType(YOUR_TURN).build();

    var aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    aPermanent.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    var selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectionItselfAsLongItsYourTurnNo() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).itself(true).turnStatusType(YOUR_TURN).build();

    var aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "opponent-name");
    aPermanent.setController("opponent-name");
    gameStatus.getNonCurrentPlayer().getBattlefield().addCard(aPermanent);

    // When
    var selection = selectorService.select(gameStatus, aPermanent, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionCurrentEnchanted() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).currentEnchanted(true).build();

    var aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name");
    aPermanent.setController("player-name");
    var theEnchantment = cardInstanceFactory.create(gameStatus, 2, cards.get("Colossification"), "player-name");
    theEnchantment.setController("player-name");
    theEnchantment.getModifiers().setAttachedToId(aPermanent.getId());

    gameStatus.getCurrentPlayer().getBattlefield().addCard(aPermanent);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(theEnchantment);

    // When
    var selection = selectorService.select(gameStatus, theEnchantment, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aPermanent);
  }

  @Test
  public void selectionHistoric() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(PERMANENT).historic(true).build();

    var aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    aCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aCreature);

    var anArtifact = cardInstanceFactory.create(gameStatus, 2, cards.get("Jousting Dummy"), "player-name");
    anArtifact.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anArtifact);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(anArtifact);
  }

  @Test
  public void selectionArtifactCreature() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(PERMANENT)
      .ofAllTypes(List.of(ARTIFACT, CREATURE))
      .build();

    var aCreature = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    aCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aCreature);

    var anArtifact = cardInstanceFactory.create(gameStatus, 1, cards.get("Brawler's Plate"), "player-name");
    anArtifact.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(aCreature);

    var anArtifactCreature = cardInstanceFactory.create(gameStatus, 2, cards.get("Jousting Dummy"), "player-name");
    anArtifactCreature.setController("player-name");
    gameStatus.getCurrentPlayer().getBattlefield().addCard(anArtifactCreature);

    // When
    var selection = selectorService.select(gameStatus, null, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(anArtifactCreature);
  }

  @Test
  public void selectionSpellEmpty() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(SPELL)
      .build();

    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "opponent-name");

    // When
    var selection = selectorService.select(gameStatus, cardInstance, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionAnySpell() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(SPELL)
      .build();

    var aSpell = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    gameStatus.getStack().push(aSpell);

    // When
    var selection = selectorService.select(gameStatus, aSpell, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(aSpell);
  }

  @Test
  public void selectionAnInstantNotMatched() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(SPELL)
      .ofType(List.of(INSTANT))
      .build();

    var aSpell = cardInstanceFactory.create(gameStatus, 1, cards.get("Grazing Whiptail"), "player-name");
    gameStatus.getStack().push(aSpell);

    // When
    var selection = selectorService.select(gameStatus, aSpell, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).isEmpty();
  }

  @Test
  public void selectionAnInstantOfSorcerySpell() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder()
      .selectorType(SPELL)
      .ofType(List.of(INSTANT, SORCERY))
      .build();

    var anInstant = cardInstanceFactory.create(gameStatus, 1, cards.get("Precision Bolt"), "player-name");
    gameStatus.getStack().push(anInstant);

    // When
    var selection = selectorService.select(gameStatus, anInstant, magicInstanceSelector).getCards();

    // Then
    assertThat(selection).containsExactly(anInstant);
  }

  @Test
  public void selectionAsTargetSkipsHexproof() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var playerHexproof = cardInstanceFactory.create(gameStatus, 1, cards.get("Cold-Water Snapper"), "player-name");
    gameStatus.getPlayer1().getBattlefield().addCard(playerHexproof);

    var playerNonHexproof = cardInstanceFactory.create(gameStatus, 2, cards.get("Yoked Ox"), "player-name");
    gameStatus.getPlayer1().getBattlefield().addCard(playerNonHexproof);

    var opponentHexproof = cardInstanceFactory.create(gameStatus, 3, cards.get("Cold-Water Snapper"), "opponent-name");
    gameStatus.getPlayer2().getBattlefield().addCard(opponentHexproof);

    var opponentNonHexproof = cardInstanceFactory.create(gameStatus, 4, cards.get("Yoked Ox"), "opponent-name");
    gameStatus.getPlayer2().getBattlefield().addCard(opponentNonHexproof);

    var playerInstant = cardInstanceFactory.create(gameStatus, 5, cards.get("Disfigure"), "player-name");
    var playerInstantSelector = playerInstant.getAbilities().get(0).getTargets().get(0).getMagicInstanceSelector();

    // When
    var selection = selectorService.selectAsTarget(gameStatus, playerInstant, playerInstantSelector).getCards();

    // Then
    assertThat(selection).containsExactly(playerHexproof, playerNonHexproof, opponentNonHexproof);
  }
}