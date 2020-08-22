package integration.deck;

import com.matag.adminentities.DeckInfo;
import com.matag.cards.Cards;
import com.matag.cards.properties.Color;
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

  @Autowired
  private Cards cards;

  @Test
  public void testFactory() {
    // Given
    var deckInfo = new DeckInfo(List.of(cards.get("Plains")));

    // When
    var cards = deckFactory.create("playerName", testUtils.testGameStatus(), deckInfo);

    // Then
    assertThat(cards).hasSize(1);
  }
}