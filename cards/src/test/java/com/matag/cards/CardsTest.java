package com.matag.cards;

import com.matag.cards.ability.Ability;
import com.matag.cards.ability.selector.CardInstanceSelector;
import com.matag.cards.ability.selector.SelectorType;
import com.matag.cards.ability.target.Target;
import com.matag.cards.ability.type.AbilityType;
import com.matag.cards.properties.*;
import com.matag.downloader.CardImageLinker;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.matag.cards.ability.trigger.Trigger;
import org.assertj.core.api.Assertions;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Arrays.asList;
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
    Assertions.assertThat(card.getColors()).containsExactly(Color.WHITE);
    assertThat(card.getCost()).containsExactly(Cost.WHITE, Cost.COLORLESS);
    assertThat(card.getTypes()).containsExactly(Type.CREATURE);
    Assertions.assertThat(card.getSubtypes()).containsExactlyInAnyOrder(Subtype.SOLDIER, Subtype.VAMPIRE);
    Assertions.assertThat(card.getRarity()).isEqualTo(Rarity.COMMON);
    assertThat(card.getRuleText()).isNullOrEmpty();
    assertThat(card.getPower()).isEqualTo(2);
    assertThat(card.getToughness()).isEqualTo(2);
  }

  @Test
  public void shouldLoadACardWithAbilities() {
    Card card = cards.get("Act of Treason");
    assertThat(card.getName()).isEqualTo("Act of Treason");
    Assertions.assertThat(card.getColors()).containsExactly(Color.RED);
    assertThat(card.getCost()).containsExactly(Cost.RED, Cost.COLORLESS, Cost.COLORLESS);
    assertThat(card.getTypes()).containsExactly(Type.SORCERY);
    Assertions.assertThat(card.getSubtypes()).isNullOrEmpty();
    Assertions.assertThat(card.getRarity()).isEqualTo(Rarity.COMMON);
    assertThat(card.getRuleText()).isEqualTo("Gain control of target creature until end of turn. Untap that creature. It gains haste until end of turn.");
    assertThat(card.getPower()).isEqualTo(0);
    assertThat(card.getToughness()).isEqualTo(0);
    assertThat(card.getAbilities()).hasSize(1);
    assertThat(card.getAbilities().get(0)).isEqualTo(
      Ability.builder()
        .abilityType(AbilityType.THAT_TARGETS_GET)
        .targets(singletonList(Target.builder()
          .cardInstanceSelector(CardInstanceSelector.builder()
            .selectorType(SelectorType.PERMANENT)
            .ofType(singletonList(Type.CREATURE))
            .build())
          .build()))
        .parameters(asList(":CONTROLLED", ":UNTAPPED", "HASTE"))
        .trigger(Trigger.castTrigger())
        .build()
    );
  }

  @Ignore
  @Test
  public void cardImageLinker() throws Exception {
    ObjectMapper objectMapper = createCardsObjectMapper();

    List<Card> cardsWithoutImage = cards.getAll().stream()
      .filter(card -> card.getImageUrls() == null)
      .collect(toList());

    for (int i = 0; i < cardsWithoutImage.size(); i++) {
      Card card = cardsWithoutImage.get(i);
      CardImageLinker cardImageLinker = new CardImageLinker(card);
      CardImageUrls imageUrls = new CardImageUrls(cardImageLinker.getLowResolution(), cardImageLinker.getHighResolution());
      Card cardWithImage = card.toBuilder().imageUrls(imageUrls).build();
      String cardJson = objectMapper.writeValueAsString(cardWithImage);
      Files.write(Paths.get(CardsConfiguration.getResourcesPath() + "/cards/" + card.getName() + ".json"), cardJson.getBytes());
      System.out.println("Downloaded " + (i + 1) + " of " + cardsWithoutImage.size());
    }
  }

  private void validateCard(Card card) {
    String name = card.getName();
    if (card.getImageUrls() == null) {
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
