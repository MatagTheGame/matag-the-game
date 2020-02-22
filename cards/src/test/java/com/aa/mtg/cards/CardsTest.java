package com.aa.mtg.cards;

import static com.aa.mtg.cards.ability.trigger.Trigger.castTrigger;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.selector.CardInstanceSelector;
import com.aa.mtg.cards.ability.selector.SelectorType;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.type.AbilityType;
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
        assertThat(cards.getAll()).isNotEmpty();
    }

    @Test
    public void shouldLoadACardWithoutAbilities() {
        Card card = cards.get("Bishop's Soldier");
        assertThat(card.getName()).isEqualTo("Bishop's Soldier");
        assertThat(card.getColors()).containsExactly(Color.WHITE);
        assertThat(card.getCost()).containsExactly(Cost.WHITE, Cost.COLORLESS);
        assertThat(card.getTypes()).containsExactly(Type.CREATURE);
        assertThat(card.getSubtypes()).containsExactlyInAnyOrder(Subtype.SOLDIER, Subtype.VAMPIRE);
        assertThat(card.getRarity()).isEqualTo(Rarity.COMMON);
        assertThat(card.getRuleText()).isNullOrEmpty();
        assertThat(card.getPower()).isEqualTo(2);
        assertThat(card.getToughness()).isEqualTo(2);
    }

    @Test
    public void shouldLoadACardWithAbilities() {
        Card card = cards.get("Act of Treason");
        assertThat(card.getName()).isEqualTo("Act of Treason");
        assertThat(card.getColors()).containsExactly(Color.RED);
        assertThat(card.getCost()).containsExactly(Cost.RED, Cost.COLORLESS, Cost.COLORLESS);
        assertThat(card.getTypes()).containsExactly(Type.SORCERY);
        assertThat(card.getSubtypes()).isNullOrEmpty();
        assertThat(card.getRarity()).isEqualTo(Rarity.COMMON);
        assertThat(card.getRuleText()).isEqualTo("Gain control of target creature until end of turn. Untap that creature. It gains haste until end of turn.");
        assertThat(card.getPower()).isEqualTo(0);
        assertThat(card.getToughness()).isEqualTo(0);
        assertThat(card.getAbilities()).hasSize(3);
        assertThat(card.getAbilities().get(0)).isEqualTo(
            Ability.builder()
                .abilityType(AbilityType.THAT_TARGETS_GET)
                .targets(singletonList(Target.builder()
                    .cardInstanceSelector(CardInstanceSelector.builder()
                        .selectorType(SelectorType.PERMANENT)
                        .ofType(singletonList(Type.CREATURE))
                        .build())
                    .build()))
                .parameters(singletonList(":CONTROLLED"))
                .trigger(castTrigger())
                .build()
        );
    }
}
