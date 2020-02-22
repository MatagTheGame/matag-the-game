package integration;

import com.aa.mtg.cardinstance.CardInstanceFactory;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.player.PlayerFactory;
import com.aa.mtg.game.stack.SpellStack;
import com.aa.mtg.game.status.GameStatusFactory;
import com.aa.mtg.game.turn.Turn;
import integration.mtg.cards.CardsTestConfiguration;
import integration.mtg.game.turn.player.PlayerTestConfiguration;
import integration.mtg.game.turn.status.GameStatusTestConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CardsTestConfiguration.class, GameStatusTestConfiguration.class, PlayerTestConfiguration.class, Turn.class, SpellStack.class})
public class TestUtilsConfiguration {
  @Bean
  public TestUtils testUtils(GameStatusFactory gameStatusFactory, PlayerFactory playerFactory, CardInstanceFactory cardInstanceFactory, Cards cards) {
    return new TestUtils(gameStatusFactory, playerFactory, cardInstanceFactory, cards);
  }

}