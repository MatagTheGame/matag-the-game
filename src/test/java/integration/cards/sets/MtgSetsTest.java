package integration.cards.sets;

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

import java.io.File;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void allCardsHaveTheirImage() {
        Set<String> nonExistingImages = new TreeSet<>();

        for (Card card : mtgSets.getAllCards()) {
            String normalizeCardName = normalizeCardName(card.getName());
            if (!new File("src/main/resources/public/img/cards/" + normalizeCardName + ".jpg").exists()) {
                nonExistingImages.add(card.getName() + " - " + normalizeCardName);
            }
        }

        assertThat(nonExistingImages).isEmpty();
    }

    private static String normalizeCardName(String cardName) {
        return cardName.toLowerCase()
                .replaceAll(" ", "_")
                .replaceAll("-", "_")
                .replaceAll(",", "")
                .replaceAll("'", "");
    }

    @Configuration
    @ComponentScan(basePackageClasses = {MtgSets.class})
    public static class MtgSetsTestConfiguration {

    }
}
