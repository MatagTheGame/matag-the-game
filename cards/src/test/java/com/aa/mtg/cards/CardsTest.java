package com.aa.mtg.cards;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Import(CardsConfiguration.class)
public class CardsTest {

    @Autowired
    private Cards cards;

    @Test
    public void shouldLoadAllCards() {
        assertThat(cards.getCards()).isNotEmpty();
        assertThat(cards.getCard("Bishop's Soldier")).isNotNull();
    }
}
