package integration.turn.action.combat;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.message.MessageException;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.combat.BlockerChecker;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CombatTestConfiguration.class)
public class BlockCheckerTest {

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
    GameStatus gameStatus = testUtils.testGameStatus();

    // When
    CardInstance boggartBrute = cardInstanceFactory.create(gameStatus, 1, cards.get("Boggart Brute"), "player");
    CardInstance firstAirElemental = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent");
    CardInstance secondAirElemental = cardInstanceFactory.create(gameStatus, 3, cards.get("Air Elemental"), "opponent");

    // Then
    blockerChecker.checkIfCanBlock(boggartBrute, List.of(firstAirElemental, secondAirElemental));
  }

  @Test(expected = MessageException.class)
  public void shouldNotBlockWhenItHasOneBlocker() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();

    // When
    CardInstance boggartBrute = cardInstanceFactory.create(gameStatus, 1, cards.get("Boggart Brute"), "player");
    CardInstance airElemental = cardInstanceFactory.create(gameStatus, 2, cards.get("Air Elemental"), "opponent");

    // Then
    blockerChecker.checkIfCanBlock(boggartBrute, List.of(airElemental));
  }

}
