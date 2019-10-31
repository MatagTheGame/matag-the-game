package com.aa.mtg.cards.sets;

import static org.assertj.core.api.Assertions.assertThat;

import com.aa.mtg.cards.CardsConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(CardsConfiguration.class)
public class MtgSetsTest {

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
}
