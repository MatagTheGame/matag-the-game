package integration.deck;

import com.matag.adminentities.DeckInfo;
import com.matag.cards.properties.Color;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.deck.DeckFactory;
import integration.TestUtils;
import integration.TestUtilsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = TestUtilsConfiguration.class)
public class DeckFactoryTest {
  @Autowired
  private TestUtils testUtils;

  @Autowired
  private DeckFactory deckFactory;

  @Test
  public void oneColorDeck() {
    // Given
    DeckInfo deckInfo = new DeckInfo(Set.of(Color.WHITE));

    // When
    List<CardInstance> cards = deckFactory.create("playerName", testUtils.testGameStatus(), deckInfo);

    // Then
    assertThat(cards).hasSize(60);
  }

  @Test
  public void twoColorsDeck() {
    // Given
    DeckInfo deckInfo = new DeckInfo(Set.of(Color.WHITE, Color.RED));

    // When
    List<CardInstance> cards = deckFactory.create("playerName", testUtils.testGameStatus(), deckInfo);

    // Then
    assertThat(cards).hasSize(60);
  }
}