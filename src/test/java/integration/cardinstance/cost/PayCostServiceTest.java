package integration.cardinstance.cost;

import com.matag.cards.Card;
import com.matag.cards.Cards;
import com.matag.cards.CardsConfiguration;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.cardinstance.cost.PayCostService;
import com.matag.game.status.GameStatus;
import com.matag.game.turn.action.tap.TapPermanentService;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
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
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = createCardInstance(gameStatus, "Feral Maaka");
    CardInstance mountain1 = createCardInstance(gameStatus, "Mountain");
    CardInstance mountain2 = createCardInstance(gameStatus, "Mountain");

    Map<Integer, List<String>> manaPaid = Map.of(
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
    GameStatus gameStatus = testUtils.testGameStatus();
    CardInstance cardInstance = createCardInstance(gameStatus, "Checkpoint Officer");
    CardInstance plains1 = createCardInstance(gameStatus, "Plains");
    CardInstance plains2 = createCardInstance(gameStatus, "Plains");
    Map<Integer, List<String>> manaPaid = Map.of(
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
    Card card = cards.get(cardName);
    CardInstance cardInstance = cardInstanceFactory.create(++cardInstanceId, card, "player-name", "player-name");
    gameStatus.getActivePlayer().getBattlefield().addCard(cardInstance);
    return cardInstance;
  }
}