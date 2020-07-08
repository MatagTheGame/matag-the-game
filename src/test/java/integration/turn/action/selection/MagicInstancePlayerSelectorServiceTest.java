package integration.turn.action.selection;

import com.matag.cards.Cards;
import com.matag.cards.ability.selector.MagicInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.turn.action.selection.MagicInstancePlayerSelectorService;
import integration.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static com.matag.player.PlayerType.OPPONENT;
import static com.matag.player.PlayerType.PLAYER;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SelectionTestConfiguration.class)
public class MagicInstancePlayerSelectorServiceTest {

  @Autowired
  private MagicInstancePlayerSelectorService selectorService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Test
  public void selectPlayer() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(SelectorType.PLAYER).itself(true).build();
    var aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name");

    // When
    var selection = selectorService.selectPlayers(gameStatus, aPermanent, magicInstanceSelector);

    // Then
    assertThat(selection).containsExactly(gameStatus.getPlayer1());
  }

  @Test
  public void selectOpponent() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(SelectorType.PLAYER).controllerType(OPPONENT).build();
    var aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name");

    // When
    var selection = selectorService.selectPlayers(gameStatus, aPermanent, magicInstanceSelector);

    // Then
    assertThat(selection).containsExactly(gameStatus.getPlayer2());
  }

  @Test
  public void selectAllPlayers() {
    // Given
    var gameStatus = testUtils.testGameStatus();

    var magicInstanceSelector = MagicInstanceSelector.builder().selectorType(SelectorType.PLAYER).controllerType(PLAYER).build();
    var aPermanent = cardInstanceFactory.create(gameStatus, 1, cards.get("Empyrean Eagle"), "player-name", "player-name");

    // When
    var selection = selectorService.selectPlayers(gameStatus, aPermanent, magicInstanceSelector);

    // Then
    assertThat(selection).containsExactlyInAnyOrder(gameStatus.getPlayer1(), gameStatus.getPlayer2());
  }
}