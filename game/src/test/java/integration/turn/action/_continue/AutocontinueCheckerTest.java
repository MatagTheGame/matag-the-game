package integration.turn.action._continue;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.AutocontinueChecker;
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
public class AutocontinueCheckerTest {
  @Autowired
  private TestUtils testUtils;

  @Autowired
  private AutocontinueChecker autocontinueChecker;

  @Autowired
  private Cards cards;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Test
  public void canPerformAnyActionReturnsFalseIfUPAndNoCardsInHandOrBattlefield() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase("UP");
    gameStatus.getPlayer1().getHand().getCards().clear();

    // When
    boolean result = autocontinueChecker.canPerformAnyAction(gameStatus);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void canPerformAnyActionReturnsTrueIfUPAndAffordableInstantInHand() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase("UP");
    gameStatus.getPlayer1().getHand().getCards().clear();
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 61, cards.get("Mountain"), "player-name", "player-name"));
    gameStatus.getPlayer1().getHand().addCard(cardInstanceFactory.create(gameStatus, 62, cards.get("Infuriate"), "player-name"));

    // When
    boolean result = autocontinueChecker.canPerformAnyAction(gameStatus);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  public void canPerformAnyActionReturnsFalseIfUPAndNotAffordableInstantInHand() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase("UP");
    gameStatus.getPlayer1().getHand().getCards().clear();
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 61, cards.get("Mountain"), "player-name", "player-name"));
    gameStatus.getPlayer1().getBattlefield().getCards().get(0).getModifiers().tap();
    gameStatus.getPlayer1().getHand().addCard(cardInstanceFactory.create(gameStatus, 62, cards.get("Infuriate"), "player-name"));

    // When
    boolean result = autocontinueChecker.canPerformAnyAction(gameStatus);

    // Then
    assertThat(result).isFalse();
  }

  @Test
  public void canPerformAnyActionReturnsFalseIfUPAndCardWithTriggeredAbility() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase("UP");
    gameStatus.getPlayer1().getHand().getCards().clear();
    gameStatus.getPlayer1().getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 62, cards.get("Exclusion Mage"), "player-name"));

    // When
    boolean result = autocontinueChecker.canPerformAnyAction(gameStatus);

    // Then
    assertThat(result).isFalse();
  }
}