package integration.cardinstance.cost;

import com.matag.cards.Cards;
import com.matag.cards.CardsConfiguration;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.cardinstance.cost.PayCostService;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.tap.TapPermanentService;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import({CardsConfiguration.class, TestUtilsConfiguration.class})
public class PayCostServiceTest {
  private int cardInstanceId = 60;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private PayCostService payCostService;

  @Autowired
  private TapPermanentService tapPermanentService;

  @Test
  public void isCastingCostFulfilledFeralMaakaCorrectCosts() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = createCardInstance(gameStatus, "Feral Maaka");
    var mountain1 = createCardInstance(gameStatus, "Mountain");
    var mountain2 = createCardInstance(gameStatus, "Mountain");

    var manaPaid = Map.of(
      mountain1.getId(), List.of("RED"),
      mountain2.getId(), List.of("RED")
    );

    // When
    payCostService.pay(gameStatus, gameStatus.getActivePlayer(), cardInstance, null, manaPaid);

    // Then
    verify(tapPermanentService).tap(gameStatus, mountain1.getId());
    verify(tapPermanentService).tap(gameStatus, mountain2.getId());
  }

  @Test
  public void isCastingCostFulfilledCheckpointOfficerTapAbility() {
    // Given
    var gameStatus = testUtils.testGameStatus();
    var cardInstance = createCardInstance(gameStatus, "Checkpoint Officer");
    var plains1 = createCardInstance(gameStatus, "Plains");
    var plains2 = createCardInstance(gameStatus, "Plains");
    var manaPaid = Map.of(
      plains1.getId(), List.of("WHITE"),
      plains2.getId(), List.of("WHITE")
    );

    // When
    payCostService.pay(gameStatus, gameStatus.getActivePlayer(), cardInstance, "THAT_TARGETS_GET", manaPaid);

    // Then
    verify(tapPermanentService).tap(gameStatus, plains1.getId());
    verify(tapPermanentService).tap(gameStatus, plains2.getId());
    verify(tapPermanentService).tap(gameStatus, cardInstance.getId());
  }

  private CardInstance createCardInstance(GameStatus gameStatus, String cardName) {
    var card = cards.get(cardName);
    var cardInstance = cardInstanceFactory.create(gameStatus, ++cardInstanceId, card, "player-name", "player-name");
    gameStatus.getActivePlayer().getBattlefield().addCard(cardInstance);
    return cardInstance;
  }
}