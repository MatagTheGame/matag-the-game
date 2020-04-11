package integration.turn.action.leave;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.cards.Cards;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.leave.DestroyPermanentService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.matag.cards.ability.type.AbilityType.INDESTRUCTIBLE;
import static com.matag.cards.ability.type.AbilityType.THAT_TARGETS_GET;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LeaveTestConfiguration.class)
public class DestroyPermanentServiceTest {

  @Autowired
  private DestroyPermanentService destroyPermanentService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Test
  public void testDestroy() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

    // When
    boolean destroyed = destroyPermanentService.destroy(gameStatus, 61);

    // Then
    assertThat(destroyed).isTrue();
    assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0);
    assertThat(gameStatus.getPlayer1().getGraveyard().getCards()).contains(cardInstance);
  }

  @Test
  public void testDestroyDontBlowUpIfCardsIsNotAnymoreInTheBattlefield() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    gameStatus.getPlayer1().getGraveyard().addCard(cardInstance);

    // When
    boolean destroyed = destroyPermanentService.destroy(gameStatus, 61);

    // Then
    assertThat(destroyed).isFalse();
    assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0);
    assertThat(gameStatus.getPlayer1().getGraveyard().getCards()).contains(cardInstance);
  }

  @Test
  public void testDestroyIndestructibleDoesNotDestroyIt() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    cardInstance.getModifiers().getAbilitiesUntilEndOfTurn().add(new CardInstanceAbility(INDESTRUCTIBLE));
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

    // When
    boolean destroyed = destroyPermanentService.destroy(gameStatus, 61);

    // Then
    assertThat(destroyed).isFalse();
    assertThat(gameStatus.getPlayer1().getBattlefield().getCards()).contains(cardInstance);
    assertThat(gameStatus.getPlayer1().getGraveyard().size()).isEqualTo(0);
  }

  @Test
  public void testReduceToughnessToZeroOfIndestructibleDestroyesIt() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    cardInstance.getModifiers().getAbilitiesUntilEndOfTurn().add(new CardInstanceAbility(INDESTRUCTIBLE));
    cardInstance.getModifiers().getAbilitiesUntilEndOfTurn().add(new CardInstanceAbility(THAT_TARGETS_GET, singletonList("-3/-3")));
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

    // When
    boolean destroyed = destroyPermanentService.destroy(gameStatus, 61);

    // Then
    assertThat(destroyed).isFalse();
    assertThat(gameStatus.getPlayer1().getBattlefield().getCards()).contains(cardInstance);
    assertThat(gameStatus.getPlayer1().getGraveyard().size()).isEqualTo(0);
  }
}