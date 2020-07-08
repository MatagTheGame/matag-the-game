package integration.turn.action.combat;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.turn.action.combat.BlockerChecker;
import integration.TestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CombatTestConfiguration.class)
public class BlockerCheckerTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private Cards cards;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private BlockerChecker blockerChecker;

  @Test
  public void shouldBlockWhenItHasTwoOrMoreBlockers() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var boggartBrute = cardInstanceFactory.create(gameStatus, 1, cards.get("Boggart Brute"), "player");
    var firstAirElemental = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent");
    var secondAirElemental = cardInstanceFactory.create(gameStatus, 3, cards.get("Air Elemental"), "opponent");

    // When
    blockerChecker.checkIfCanBlock(boggartBrute, List.of(firstAirElemental, secondAirElemental));

    // Then no exception is thrown
  }

  @Test
  public void shouldNotBlockWhenItHasOneBlocker() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var boggartBrute = cardInstanceFactory.create(gameStatus, 1, cards.get("Boggart Brute"), "player");
    var airElemental = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent");

    // Expect
    thrown.expectMessage("\"2 - Air Elemental\" cannot block \"1 - Boggart Brute\" alone as it has menace.");

    // When
    blockerChecker.checkIfCanBlock(boggartBrute, List.of(airElemental));
  }
}
