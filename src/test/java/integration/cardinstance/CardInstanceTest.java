package integration.cardinstance;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

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
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), "player-name");

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldThrowExceptionForNonCreatures() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Short Sword"), "player-name");

    // Expects
    thrown.expectMessage("\"1 - Short Sword\" is not of type Creature.");

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldThrowExceptionForTappedCreatures() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), "player-name");
    cardInstance.getModifiers().setTapped(true);

    // Expects
    thrown.expectMessage("\"1 - Feral Maaka\" is tapped and cannot attack.");

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldThrowExceptionForCreaturesWithSummoningSickness() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Feral Maaka"), "player-name");
    cardInstance.getModifiers().setSummoningSickness(true);

    // Expects
    thrown.expectMessage("\"1 - Feral Maaka\" has summoning sickness and cannot attack.");

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldBeOkForCreaturesWithSummoningSicknessButHaste() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Nest Robber"), "player-name");
    cardInstance.getModifiers().setSummoningSickness(true);

    // When
    cardInstance.checkIfCanAttack();
  }

  @Test
  public void checkIfCanAttackShouldThrowExceptionForCreaturesWithDefender() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 1, cards.get("Guardians of Meletis"), "player-name");

    // Expects
    thrown.expectMessage("\"1 - Guardians of Meletis\" has defender and cannot attack.");

    // When
    cardInstance.checkIfCanAttack();
  }
}