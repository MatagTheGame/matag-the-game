package integration.turn.action._continue;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.turn.action._continue.ConsolidateStatusService;
import integration.TestUtils;
import integration.turn.action.leave.LeaveTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ContinueTestConfiguration.class, LeaveTestConfiguration.class})
public class ConsolidateStatusServiceTest {
  @Autowired
  private TestUtils testUtils;

  @Autowired
  private ConsolidateStatusService consolidateStatusService;

  @Autowired
  private Cards cards;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Test
  public void consolidateShouldReturnACreatureToHandAndClearTheModifiers() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    cardInstance.getModifiers().getModifiersUntilEndOfTurn().setToBeReturnedToHand(true);
    cardInstance.getModifiers().dealDamage(1);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

    // When
    consolidateStatusService.consolidate(gameStatus);

    // Then
    assertThat(gameStatus.getCurrentPlayer().getBattlefield().getCards()).hasSize(0);
    assertThat(gameStatus.getCurrentPlayer().getHand().getCards()).contains(cardInstance);
    assertThat(cardInstance.getModifiers().getDamage()).isEqualTo(0);
  }

  @Test
  public void consolidateShouldDestroyACreatureAndClearTheModifiers() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    cardInstance.getModifiers().getModifiersUntilEndOfTurn().setToBeDestroyed(true);
    cardInstance.getModifiers().dealDamage(1);
    gameStatus.getCurrentPlayer().getBattlefield().addCard(cardInstance);

    // When
    consolidateStatusService.consolidate(gameStatus);

    // Then
    assertThat(gameStatus.getCurrentPlayer().getBattlefield().getCards()).hasSize(0);
    assertThat(gameStatus.getCurrentPlayer().getGraveyard().getCards()).contains(cardInstance);
    assertThat(cardInstance.getModifiers().getDamage()).isEqualTo(0);
  }
}