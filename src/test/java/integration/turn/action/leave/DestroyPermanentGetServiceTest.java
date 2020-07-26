package integration.turn.action.leave;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.cardinstance.ability.CardInstanceAbility;
import com.matag.game.turn.action.leave.DestroyPermanentService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.matag.cards.ability.type.AbilityType.INDESTRUCTIBLE;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = LeaveTestConfiguration.class)
public class DestroyPermanentGetServiceTest {

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
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

    // When
    var destroyed = destroyPermanentService.destroy(gameStatus, 61);

    // Then
    assertThat(destroyed).isTrue();
    assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0);
    assertThat(gameStatus.getPlayer1().getGraveyard().getCards()).contains(cardInstance);
  }

  @Test
  public void testDestroyDontBlowUpIfCardsIsNotAnymoreInTheBattlefield() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    gameStatus.getPlayer1().getGraveyard().addCard(cardInstance);

    // When
    var destroyed = destroyPermanentService.destroy(gameStatus, 61);

    // Then
    assertThat(destroyed).isFalse();
    assertThat(gameStatus.getPlayer1().getBattlefield().size()).isEqualTo(0);
    assertThat(gameStatus.getPlayer1().getGraveyard().getCards()).contains(cardInstance);
  }

  @Test
  public void testDestroyIndestructibleDoesNotDestroyIt() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = cardInstanceFactory.create(gameStatus, 61, cards.get("Canopy Spider"), "player-name", "player-name");
    cardInstance.getModifiers().getModifiersUntilEndOfTurn().getExtraAbilities().add(new CardInstanceAbility(INDESTRUCTIBLE));
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstance);

    // When
    var destroyed = destroyPermanentService.destroy(gameStatus, 61);

    // Then
    assertThat(destroyed).isFalse();
    assertThat(gameStatus.getPlayer1().getBattlefield().getCards()).contains(cardInstance);
    assertThat(gameStatus.getPlayer1().getGraveyard().size()).isEqualTo(0);
  }

  @Test
  public void testReduceToughnessToZeroOfIndestructibleDestroysIt() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var aCreature = cardInstanceFactory.create(gameStatus, 61, cards.get("Seraph of the Suns"), "player-name", "player-name");
    var instantMinus4 = cardInstanceFactory.create(gameStatus, 62, cards.get("Grasp of Darkness"), "player-name", "player-name");
    aCreature.getModifiers().getModifiersUntilEndOfTurn().getExtraAbilities().add(instantMinus4.getAbilities().get(0));
    gameStatus.getPlayer1().getBattlefield().addCard(aCreature);

    // When
    var destroyed = destroyPermanentService.destroy(gameStatus, 61);

    // Then
    assertThat(destroyed).isFalse();
    assertThat(gameStatus.getPlayer1().getBattlefield().getCards()).contains(aCreature);
    assertThat(gameStatus.getPlayer1().getGraveyard().size()).isEqualTo(0);
  }
}