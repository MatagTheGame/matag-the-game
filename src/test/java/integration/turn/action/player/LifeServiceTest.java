package integration.turn.action.player;

import com.matag.game.turn.action.finish.FinishGameService;
import com.matag.game.turn.action.player.LifeService;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PlayerTestConfiguration.class, TestUtilsConfiguration.class})
public class LifeServiceTest {

  @Autowired
  private LifeService lifeService;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private FinishGameService finishGameService;

  @Test
  public void addLife() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var amount = 1;

    // When
    lifeService.add(gameStatus.getPlayer1(), amount, gameStatus);

    // Then
    assertThat(gameStatus.getPlayer1().getLife()).isEqualTo(21);
  }

  @Test
  public void loseLifeAndLoseGame() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var amount = -25;

    // When
    lifeService.add(gameStatus.getPlayer1(), amount, gameStatus);

    // Then
    assertThat(gameStatus.getPlayer1().getLife()).isEqualTo(-5);
    verify(finishGameService).setWinner(gameStatus, gameStatus.getPlayer2());
  }
}