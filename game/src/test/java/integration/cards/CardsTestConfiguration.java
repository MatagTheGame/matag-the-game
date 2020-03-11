package integration.cards;

import com.matag.cardinstance.CardInstanceConfiguration;
import com.matag.cards.CardsConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({CardsConfiguration.class, CardInstanceConfiguration.class})
public class CardsTestConfiguration {

}