package integration.cards;

import application.AbstractApplicationTest;
import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.sets.MtgSets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MtgSetsTest.MtgSetsTestConfiguration.class)
@Configuration
public class MtgSetsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractApplicationTest.class);

    @Autowired
    private MtgSets mtgSets;

    @Test
    public void countCards() {
        LOGGER.info("Num of Cards: " + mtgSets.getAllCards().size());
        LOGGER.info("Cards by Colors: " + mtgSets.getAllCards().stream().collect(groupingBy(Card::getColors, counting())));
        LOGGER.info("Cards by Types: " + mtgSets.getAllCards().stream().collect(groupingBy(Card::getTypes, counting())));
        LOGGER.info("Cards by Rarity: " + mtgSets.getAllCards().stream().collect(groupingBy(Card::getRarity, counting())));
        LOGGER.info("Cards by Set: " + mtgSets.getAllCards().stream().collect(groupingBy(card -> mtgSets.getSetForCard(card).getName(), counting())));
    }

    @Configuration
    @ComponentScan(basePackageClasses = {MtgSets.class})
    public static class MtgSetsTestConfiguration {

    }
}
