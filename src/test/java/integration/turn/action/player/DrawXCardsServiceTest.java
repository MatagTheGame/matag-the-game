package integration.turn.action.player;

import com.matag.game.turn.action.finish.FinishGameService;
import com.matag.game.turn.action.player.DrawXCardsService;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PlayerTestConfiguration.class, TestUtilsConfiguration.class})
public class DrawXCardsServiceTest {

  @Autowired
  private DrawXCardsService drawXCardsService;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private FinishGameService finishGameService;

  @Test
  public void drawCards() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var amount = 2;

    // When
    drawXCardsService.drawXCards(gameStatus.getPlayer1(), amount, gameStatus);

    // Then
    assertThat(gameStatus.getPlayer1().getHand().getCards()).hasSize(9);
    assertThat(gameStatus.getPlayer1().getLibrary().getCards()).hasSize(31);
  }

  @Test
  public void drawFromEmptyLibrary() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var firstTwoCards = gameStatus.getPlayer1().getLibrary().getCards().subList(0, 2);
    gameStatus.getPlayer1().getLibrary().setCards(firstTwoCards);
    var amount = 3;

    // When
    drawXCardsService.drawXCards(gameStatus.getPlayer1(), amount, gameStatus);

    // Then
    assertThat(gameStatus.getPlayer1().getHand().getCards()).hasSize(9);
    assertThat(gameStatus.getPlayer1().getLibrary().getCards()).hasSize(0);
    verify(finishGameService).setWinner(gameStatus, gameStatus.getPlayer2());
  }
}