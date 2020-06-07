package integration.turn.action.leave;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.attach.AttachService;
import com.matag.game.turn.action.leave.LeaveBattlefieldService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LeaveTestConfiguration.class)
public class LeaveBattlefieldServiceTest {

  @Autowired
  private LeaveBattlefieldService leaveBattlefieldService;

  @Autowired
  private AttachService attachService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Test
  public void testLeaveBattlefield() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    cardInstance.getModifiers().tap();
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

    // When
    leaveBattlefieldService.leaveTheBattlefield(gameStatus, 61);

    // Then
    assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0);
    assertThat(cardInstance.getModifiers().isTapped()).isFalse();
  }

  @Test
  public void testLeaveBattlefieldWithAttachments() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance creature = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    gameStatus.getPlayer1().getBattlefield().addCard(creature);

    CardInstance enchantment1 = cardInstanceFactory.create(gameStatus, 62, cards.get("Knight's Pledge"), "player-name", "player-name");
    gameStatus.getPlayer1().getBattlefield().addCard(enchantment1);
    attachService.attach(gameStatus, enchantment1, creature.getId());

    CardInstance enchantment2 = cardInstanceFactory.create(gameStatus, 63, cards.get("Knight's Pledge"), "opponent-name", "opponent-name");
    gameStatus.getPlayer2().getBattlefield().addCard(enchantment2);
    attachService.attach(gameStatus, enchantment2, creature.getId());

    CardInstance equipment = cardInstanceFactory.create(gameStatus, 64, cards.get("Marauder's Axe"), "player-name", "player-name");
    gameStatus.getPlayer1().getBattlefield().addCard(equipment);
    attachService.attach(gameStatus, equipment, creature.getId());

    // When
    leaveBattlefieldService.leaveTheBattlefield(gameStatus, 61);

    // Then
    assertThat(gameStatus.getPlayer1().getBattlefield().getCards()).containsExactlyInAnyOrder(equipment, enchantment1);
    assertThat(enchantment1.getModifiers().getModifiersUntilEndOfTurn().isToBeDestroyed()).isTrue();
    assertThat(equipment.getModifiers().getAttachedToId()).isEqualTo(0);
  }
}