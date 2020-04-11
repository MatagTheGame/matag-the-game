package integration;

import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.cards.Cards;
import com.matag.game.player.PlayerFactory;
import com.matag.game.stack.SpellStack;
import com.matag.game.status.GameStatusFactory;
import com.matag.game.turn.Turn;
import integration.cardinstance.CardsTestConfiguration;
import integration.turn.player.PlayerTestConfiguration;
import integration.turn.status.GameStatusTestConfiguration;
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