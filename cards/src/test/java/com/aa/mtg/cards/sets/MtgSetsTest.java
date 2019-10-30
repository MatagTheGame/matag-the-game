package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.CardsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(CardsConfiguration.class)
public class MtgSetsTest {

    @Autowired
    private MtgSets mtgSets;

    @Test
    public void shouldLoadAllSets() {
        assertThat(mtgSets.getSets()).isNotEmpty();
        assertThat(mtgSets.getSet("M20").getName()).isEqualTo("Core Set 2020");
    }
}
