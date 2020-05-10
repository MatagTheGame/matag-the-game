package integration.turn.action.cast;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.cast.PlayLandService;
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService;
import integration.TestUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.matag.game.turn.phases.FirstStrikePhase.FS;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CastTestConfiguration.class)
public class PlayLandServiceTest {

  @Autowired
  private PlayLandService playLandService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private EnterCardIntoBattlefieldService enterCardIntoBattlefieldService;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void playLand() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance card = cardInstanceFactory.create(100, cards.get("Swamp"), "player-name");
    gameStatus.getPlayer1().getHand().addCard(card);

    // When
    playLandService.playLand(gameStatus, card.getId());

    // Then
    verify(enterCardIntoBattlefieldService).enter(gameStatus, card);
  }

  @Test
  public void playLandErrorNotALand() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance card = cardInstanceFactory.create(100, cards.get("Befuddle"), "player-name");
    gameStatus.getPlayer1().getHand().addCard(card);

    // Expect
    thrown.expectMessage("Playing \"100 - Befuddle\" as land.");

    // When
    playLandService.playLand(gameStatus, card.getId());
  }

  @Test
  public void playLandMultipleLand() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance card1 = cardInstanceFactory.create(100, cards.get("Swamp"), "player-name");
    gameStatus.getPlayer1().getHand().addCard(card1);
    CardInstance card2 = cardInstanceFactory.create(101, cards.get("Swamp"), "player-name");
    gameStatus.getPlayer1().getHand().addCard(card2);

    // When
    playLandService.playLand(gameStatus, card1.getId());

    // Then
    verify(enterCardIntoBattlefieldService).enter(gameStatus, card1);

    // Expect
    thrown.expectMessage("You already played a land this turn.");

    // When
    playLandService.playLand(gameStatus, card2.getId());
  }

  @Test
  public void playLandNotInMainPhase() {
    // Given
    GameStatus gameStatus = testUtils.testGameStatus();
    gameStatus.getTurn().setCurrentPhase(FS);
    CardInstance card = cardInstanceFactory.create(100, cards.get("Swamp"), "player-name");
    gameStatus.getPlayer1().getHand().addCard(card);

    // Expect
    thrown.expectMessage("You can only play lands during main phases.");

    // When
    playLandService.playLand(gameStatus, card.getId());
  }
}