package com.aa.mtg.cards;

import com.aa.mtg.cards.ability.Ability;
import com.aa.mtg.cards.ability.selector.CardInstanceSelector;
import com.aa.mtg.cards.ability.selector.SelectorType;
import com.aa.mtg.cards.ability.target.Target;
import com.aa.mtg.cards.ability.type.AbilityType;
import com.aa.mtg.cards.properties.*;
import com.aa.mtg.downloader.CardImageLinker;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.platform.commons.util.StringUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.aa.mtg.cards.CardsConfiguration.getResourcesPath;
import static com.aa.mtg.cards.ability.trigger.Trigger.castTrigger;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Import(CardsConfiguration.class)
public class CardsTest {

  @Autowired
  private Cards cards;

  @Test
  public void shouldLoadAllCards() {
    assertThat(cards.getAll()).isNotEmpty();

    for (Card card : cards.getAll()) {
      validateCard(card);
    }
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

  @Ignore
  @Test
  public void cardImageLinker() throws Exception {
    ObjectMapper objectMapper = createCardsObjectMapper();

    List<Card> cardsWithoutImage = cards.getAll().stream()
      .filter(card -> StringUtils.isBlank(card.getImageUrl()))
      .collect(toList());

    for (int i = 0; i < cardsWithoutImage.size(); i++) {
      Card card = cardsWithoutImage.get(i);
      String imageUrl = new CardImageLinker(card).getImageUrl();
      Card cardWithImage = card.toBuilder().imageUrl(imageUrl).build();
      String cardJson = objectMapper.writeValueAsString(cardWithImage);
      Files.write(Paths.get(getResourcesPath() + "/cards/" + card.getName() + ".json"), cardJson.getBytes());
      System.out.println("Downloaded " + (i + 1) + " of " + cardsWithoutImage.size());
    }
  }

  private void validateCard(Card card) {
    String name = card.getName();
    if (StringUtils.isBlank(card.getImageUrl())) {
      throw new RuntimeException("Card '" + name + "' does not have an imageUrl. Run cardImageLinker");
    }

    if (card.getTypes().isEmpty()) {
      throw new RuntimeException("Card '" + name + "' does not have a type");
    }

    if (card.getRarity() == null) {
      throw new RuntimeException("Card '" + name + "' does not have rarity");
    }
  }

  public ObjectMapper createCardsObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    objectMapper.setDefaultPropertyInclusion(JsonInclude.Include.NON_DEFAULT);
    return objectMapper;
  }
}
