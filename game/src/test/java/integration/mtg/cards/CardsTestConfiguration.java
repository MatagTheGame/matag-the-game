package integration.mtg.cards;

import com.aa.mtg.cardinstance.CardInstanceConfiguration;
import com.aa.mtg.cards.CardsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CardsConfiguration.class, CardInstanceConfiguration.class})
public class CardsTestConfiguration {

}