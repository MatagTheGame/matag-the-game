package integration.turn.action.cast;

import com.matag.cards.Cards;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.turn.action.cast.ManaCountService;
import integration.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

import static com.matag.cards.properties.Cost.BLUE;
import static com.matag.cards.properties.Cost.WHITE;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CastTestConfiguration.class)
public class ManaCountServiceTest {

  @Autowired
  private ManaCountService manaCountService;

  @Autowired
  private CardInstanceFactory cardInstanceFactory;

  @Autowired
  private TestUtils testUtils;

  @Autowired
  private Cards cards;

//  @Rule
//  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void countManaPaidForSimpleLands() {
    // Given
    var mana = Map.of(
      1, List.of("WHITE"),
      2, List.of("WHITE"),
      3, List.of("BLUE")
    );
    var gameStatus = testUtils.testGameStatus();
    var player = gameStatus.getPlayer1();

    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), player.getName(), player.getName()));
    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 2, cards.get("Plains"), player.getName(), player.getName()));
    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 3, cards.get("Island"), player.getName(), player.getName()));

    // When
    var colors = manaCountService.verifyManaPaid(mana, player);

    // Then
    assertThat(colors).containsExactlyInAnyOrder(WHITE, WHITE, BLUE);
  }

//  @Test
//  public void countManaPaidTappingInstant() {
//    // Given
//    var mana = Map.of(
//      1, List.of("WHITE")
//    );
//    var gameStatus = testUtils.testGameStatus();
//    var player = gameStatus.getPlayer1();
//
//    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Dark Remedy"), player.getName(), player.getName()));
//
//    thrown.expectMessage("\"1 - Dark Remedy\" cannot be tapped for mana.");
//
//    // When
//    manaCountService.verifyManaPaid(mana, player);
//  }

//  @Test
//  public void countManaPaidTappingAlreadyTappedLand() {
//    // Given
//    var mana = Map.of(
//      1, List.of("WHITE")
//    );
//    var gameStatus = testUtils.testGameStatus();
//    var player = gameStatus.getPlayer1();
//
//    var plains = cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), player.getName(), player.getName());
//    plains.getModifiers().setTapped(true);
//    player.getBattlefield().addCard(plains);
//
//    thrown.expectMessage("\"1 - Plains\" is already tapped.");
//
//    // When
//    manaCountService.verifyManaPaid(mana, player);
//  }

//  @Test
//  public void countManaPaidTappingLandForWrongColor() {
//    // Given
//    var mana = Map.of(1, List.of("BLUE"));
//    var gameStatus = testUtils.testGameStatus();
//    var player = gameStatus.getPlayer1();
//
//    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), player.getName(), player.getName()));
//
//    thrown.expectMessage("\"1 - Plains\" cannot produce BLUE");
//
//    // When
//    manaCountService.verifyManaPaid(mana, player);
//  }

  @Test
  public void countManaPaidTappingLandForDualLand() {
    // Given
    var mana = Map.of(
      1, List.of("BLUE")
    );
    var gameStatus = testUtils.testGameStatus();
    var player = gameStatus.getPlayer1();

    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Azorius Guildgate"), player.getName(), player.getName()));

    // When
    var colors = manaCountService.verifyManaPaid(mana, player);

    // Then
    assertThat(colors).isEqualTo(List.of(BLUE));
  }

//  @Test
//  public void countManaPaidTappingLandForDualLandError() {
//    // Given
//    var mana = Map.of(
//      1, List.of("BLACK")
//    );
//    var gameStatus = testUtils.testGameStatus();
//    var player = gameStatus.getPlayer1();
//
//    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Azorius Guildgate"), player.getName(), player.getName()));
//
//    thrown.expectMessage("\"1 - Azorius Guildgate\" cannot produce BLACK");
//
//    // When
//    manaCountService.verifyManaPaid(mana, player);
//  }

  @Test
  public void countManaPaidColorlessMana() {
    // Given
    var mana = Map.of(
      1, List.of("WHITE"),
      2, List.of("COLORLESS")
    );
    var gameStatus = testUtils.testGameStatus();
    var player = gameStatus.getPlayer1();

    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Plains"), player.getName(), player.getName()));
    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 2, cards.get("Sunscorched Desert"), player.getName(), player.getName()));

    // When
    manaCountService.verifyManaPaid(mana, player);
  }

  @Test
  public void countManaPaidTappingCreatureWhichGeneratesTwoMana() {
    // Given
    var mana = Map.of(
      1, List.of("GREEN", "BLUE")
    );
    var gameStatus = testUtils.testGameStatus();
    var player = gameStatus.getPlayer1();

    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Gyre Engineer"), player.getName(), player.getName()));

    // When
    manaCountService.verifyManaPaid(mana, player);
  }

//  @Test
//  public void countManaPaidTappingCreatureWhichGeneratesTwoManaException() {
//    // Given
//    var mana = Map.of(
//      1, List.of("GREEN", "BLACK")
//    );
//    var gameStatus = testUtils.testGameStatus();
//    var player = gameStatus.getPlayer1();
//
//    player.getBattlefield().addCard(cardInstanceFactory.create(gameStatus, 1, cards.get("Gyre Engineer"), player.getName(), player.getName()));
//
//    thrown.expectMessage("\"1 - Gyre Engineer\" cannot produce BLACK");
//
//    // When
//    manaCountService.verifyManaPaid(mana, player);
//  }
}