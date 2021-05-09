package integration.cardinstance;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;

import integration.TestUtils;
import integration.TestUtilsConfiguration;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestUtilsConfiguration.class)
public class CardInstanceTest {
  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private Cards cards;

  @Autowired
  private TestUtils testUtils;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void checkIfCanAttackShouldWorkForCreatures() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), "player-name");

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldThrowExceptionForNonCreatures() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Short Sword"), "player-name");

    // Expects
    thrown.expectMessage("\"1 - Short Sword\" is not of type Creature.");

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldThrowExceptionForTappedCreatures() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), "player-name");
    cardInstance.getModifiers().setTapped(true);

    // Expects
    thrown.expectMessage("\"1 - Feral Maaka\" is tapped and cannot attack.");

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldThrowExceptionForCreaturesWithSummoningSickness() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), "player-name");
    cardInstance.getModifiers().setSummoningSickness(true);

    // Expects
    thrown.expectMessage("\"1 - Feral Maaka\" has summoning sickness and cannot attack.");

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldBeOkForCreaturesWithSummoningSicknessButHaste() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Nest Robber"), "player-name");
    cardInstance.getModifiers().setSummoningSickness(true);

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldThrowExceptionForCreaturesWithDefender() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Guardians of Meletis"), "player-name");

    // Expects
    thrown.expectMessage("\"1 - Guardians of Meletis\" has defender and cannot attack.");

    // When
    cardInstance.checkIfCanAttack();
  }
}