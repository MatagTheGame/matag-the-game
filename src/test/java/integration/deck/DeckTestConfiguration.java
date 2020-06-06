package integration.deck;

import com.matag.game.deck.DeckFactory;
import integration.cardinstance.CardsTestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = DeckFactory.class)
@Import({CardsTestConfiguration.class})
public class DeckTestConfiguration {

}