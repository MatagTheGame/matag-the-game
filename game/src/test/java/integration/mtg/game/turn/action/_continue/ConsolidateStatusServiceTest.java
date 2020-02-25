package integration.mtg.game.turn.action._continue;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.cardinstance.CardInstanceFactory;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action._continue.ConsolidateStatusService;
import integration.TestUtils;
import integration.mtg.game.turn.action.leave.LeaveTestConfiguration;
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
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    cardInstance.setToBeReturnedToHand(true);
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
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    cardInstance.setToBeDestroyed(true);
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