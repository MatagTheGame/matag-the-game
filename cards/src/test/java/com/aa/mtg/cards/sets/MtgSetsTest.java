package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.cards.CardsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(CardsConfiguration.class)
public class MtgSetsTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(MtgSetsTest.class);

    @Autowired
    private Cards cards;

    @Autowired
    private MtgSets mtgSets;

    @Test
    public void shouldLoadAllSets() {
        assertThat(mtgSets.getSets()).isNotEmpty();
    }

    @Test
    public void shouldLoadASet() {
        MtgSet m20 = mtgSets.getSet("M20");
        assertThat(m20.getCode()).isEqualTo("M20");
        assertThat(m20.getName()).isEqualTo("Core Set 2020");
        assertThat(m20.getCards()).contains("Bladebrand");
    }

    public void countCards() {
        LOGGER.info("Num of Cards: " + cards.getAll().size());
        LOGGER.info("Cards by Colors: " + cards.getAll().stream().collect(groupingBy(Card::getColors, counting())));
        LOGGER.info("Cards by Types: " + cards.getAll().stream().collect(groupingBy(Card::getTypes, counting())));
        LOGGER.info("Cards by Rarity: " + cards.getAll().stream().collect(groupingBy(Card::getRarity, counting())));
        LOGGER.info("Cards by Set: " + countCardsBySet(mtgSets.getSets()));
    }



    private Map<String, Integer> countCardsBySet(Map<String, MtgSet> sets) {
        Map<String, Integer> countCardsBySet = new HashMap<>();

        for (String setName : sets.keySet()) {
            countCardsBySet.put(setName, sets.get(setName).getCards().size());
        }

        return countCardsBySet;
    }
}
