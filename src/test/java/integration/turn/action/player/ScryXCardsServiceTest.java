package integration.turn.action.player;

import com.matag.cards.Card;
import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.player.ScryXCardsService;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {PlayerTestConfiguration.class, TestUtilsConfiguration.class})
public class ScryXCardsServiceTest {
  private int cardInstanceId = 60;

  @Autowired
  private ScryXCardsService scryXCardsService;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Test
  public void scry1CardWithoutChangingOrder() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    gameStatus.getCurrentPlayer().getLibrary().getCards().clear();

    gameStatus.getCurrentPlayer().getLibrary().addCards(List.of(
      createCardInstance(gameStatus, "Plains"),
      createCardInstance(gameStatus, "Island"),
      createCardInstance(gameStatus, "Swamp"),
      createCardInstance(gameStatus, "Mountain"),
      createCardInstance(gameStatus, "Forest")
    ));

    // When
    scryXCardsService.scryXCards(gameStatus, List.of(1));

    // Then
    assertCards(gameStatus, "Plains", "Island", "Swamp", "Mountain", "Forest");
  }

  @Test
  public void scry1CardPutToBottom() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    gameStatus.getCurrentPlayer().getLibrary().getCards().clear();

    gameStatus.getCurrentPlayer().getLibrary().addCards(List.of(
      createCardInstance(gameStatus, "Plains"),
      createCardInstance(gameStatus, "Island"),
      createCardInstance(gameStatus, "Swamp"),
      createCardInstance(gameStatus, "Mountain"),
      createCardInstance(gameStatus, "Forest")
    ));

    // When
    scryXCardsService.scryXCards(gameStatus, List.of(-1));

    // Then
    assertCards(gameStatus, "Island", "Swamp", "Mountain", "Forest", "Plains");
  }

  @Test
  public void scry4Cards() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    gameStatus.getCurrentPlayer().getLibrary().getCards().clear();

    gameStatus.getCurrentPlayer().getLibrary().addCards(List.of(
      createCardInstance(gameStatus, "Plains"),
      createCardInstance(gameStatus, "Island"),
      createCardInstance(gameStatus, "Swamp"),
      createCardInstance(gameStatus, "Mountain"),
      createCardInstance(gameStatus, "Forest")
    ));

    // When
    scryXCardsService.scryXCards(gameStatus, List.of(2, -1, -2, 1));

    // Then
    assertCards(gameStatus, "Mountain", "Plains", "Forest", "Island", "Swamp");
  }

  private CardInstance createCardInstance(GameStatus gameStatus, String cardName) {
    var card = cards.get(cardName);
    var cardInstance = cardInstanceFactory.create(gameStatus, ++cardInstanceId, card, "player-name", "player-name");
    gameStatus.getActivePlayer().getBattlefield().addCard(cardInstance);
    return cardInstance;
  }

  private void assertCards(GameStatus gameStatus, String... cardNames) {
    var actualCardNames = gameStatus.getCurrentPlayer().getLibrary().getCards().stream()
      .map(CardInstance::getCard).map(Card::getName)
      .collect(Collectors.toList());

    assertThat(actualCardNames).containsExactly(cardNames);
  }
}