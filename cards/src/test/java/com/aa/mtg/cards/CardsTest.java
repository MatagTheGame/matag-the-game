package com.aa.mtg.cards;

import static org.assertj.core.api.Assertions.assertThat;

import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import com.aa.mtg.cards.properties.Rarity;
import com.aa.mtg.cards.properties.Subtype;
import com.aa.mtg.cards.properties.Type;
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

        Card card = cards.getCard("Bishop's Soldier");
        assertThat(card.getName()).isEqualTo("Bishop's Soldier");
        assertThat(card.getColors()).containsExactly(Color.WHITE);
        assertThat(card.getCost()).containsExactly(Cost.WHITE, Cost.COLORLESS);
        assertThat(card.getTypes()).containsExactly(Type.CREATURE);
        assertThat(card.getSubtypes()).containsExactly(Subtype.SOLDIER, Subtype.VAMPIRE);
        assertThat(card.getRarity()).isEqualTo(Rarity.COMMON);
        assertThat(card.getRuleText()).isNullOrEmpty();
        assertThat(card.getPower()).isEqualTo(2);
        assertThat(card.getToughness()).isEqualTo(2);
    }
}
