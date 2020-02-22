package integration.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.cards.sets.MtgSet;
import com.aa.mtg.cards.sets.MtgSets;
import integration.mtg.cards.CardsTestConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CardsTestConfiguration.class)
@Configuration
public class MtgSetsTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MtgSetsTest.class);

    @Autowired
    private Cards cards;

    @Autowired
    private MtgSets mtgSets;

    @Test
    public void countCards() {
        LOGGER.info("Num of Cards: " + cards.getAll().size());
        LOGGER.info("Cards by Colors: " + cards.getAll().stream().collect(groupingBy(Card::getColors, counting())));
        LOGGER.info("Cards by Types: " + cards.getAll().stream().collect(groupingBy(Card::getTypes, counting())));
        LOGGER.info("Cards by Rarity: " + cards.getAll().stream().collect(groupingBy(Card::getRarity, counting())));
        LOGGER.info("Cards by Set: " + countCardsBySet(mtgSets.getSets()));
    }

    @Test
    public void allCardsHaveTheirImage() {
        Set<String> nonExistingImages = new TreeSet<>();

        for (Card card : cards.getAll()) {
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

    private Map<String, Integer> countCardsBySet(Map<String, MtgSet> sets) {
        Map<String, Integer> countCardsBySet = new HashMap<>();

        for (String setName : sets.keySet()) {
            countCardsBySet.put(setName, sets.get(setName).getCards().size());
        }

        return countCardsBySet;
    }
}
