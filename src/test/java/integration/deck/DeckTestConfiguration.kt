package integration.deck;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.matag.game.deck.DeckFactory;

import integration.cardinstance.CardsTestConfiguration;

@Configuration
@ComponentScan(basePackageClasses = DeckFactory.class)
@Import({CardsTestConfiguration.class})
public class DeckTestConfiguration {

}