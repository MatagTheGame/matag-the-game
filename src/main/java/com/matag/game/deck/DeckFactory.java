package com.matag.game.deck;

import com.matag.adminentities.DeckInfo;
import com.matag.cards.Card;
import com.matag.cards.Cards;
import com.matag.cards.properties.Color;
import com.matag.cards.properties.Type;
import com.matag.cards.search.CardSearch;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static com.matag.cards.ability.type.AbilityType.TAP_ADD_MANA;
import static java.util.Collections.nCopies;

@Component
@AllArgsConstructor
public class DeckFactory {
  private final CardInstanceFactory cardInstanceFactory;
  private final Cards cards;

  public List<CardInstance> create(String playerName, GameStatus gameStatus, DeckInfo deckInfo) {
    return Optional.ofNullable(deckInfo)
      .map(DeckInfo::getRandomColors)
      .map(this::randomColorsCards)   // TODO this can be a flatmap
      .orElseThrow(UnsupportedOperationException::new)
      .stream()
      .map(card -> cardInstanceFactory.create(gameStatus, gameStatus.nextCardId(), card, playerName))
      .collect(Collectors.toList());
  }

  private List<Card> randomColorsCards(Set<Color> randomColors) {
    List<Card> cards = new ArrayList<>();

    // Lands
    for (int i = 0; i < 22; i++) {
      Color color = new ArrayList<>(randomColors).get(i % randomColors.size());
      cards.add(getBasicLandForColor(color));
    }
    cards.addAll(nCopies(2, getRandomNonBasicLandOfTheseColors(randomColors)));

    // Spells
    for (Card randomCard : getRandomSpellsForColors(randomColors)) {
      cards.addAll(nCopies(4, randomCard));
    }
    cards.addAll(nCopies(4, getRandomColorlessCard()));

    Collections.shuffle(cards);

    return cards;
  }

  private Card getBasicLandForColor(Color color) {
    switch (color) {
      case WHITE:
        return cards.get("Plains");
      case BLUE:
        return cards.get("Island");
      case BLACK:
        return cards.get("Swamp");
      case RED:
        return cards.get("Mountain");
      case GREEN:
        return cards.get("Forest");
      default:
        throw new RuntimeException("Basic Land for color " + color + " does not exist.");
    }
  }

  private List<Card> getRandomSpellsForColors(Set<Color> deckColors) {
    ArrayList<Card> selectedCards = new ArrayList<>();

    List<Card> creatureCardsOfTheseColors = new CardSearch(cards.getAll())
      .ofOnlyAnyOfTheColors(deckColors)
      .ofType(Type.CREATURE)
      .getCards();
    Collections.shuffle(creatureCardsOfTheseColors);
    selectedCards.addAll(creatureCardsOfTheseColors.subList(0, 5));

    List<Card> nonCreatureCardsOfTheseColors = new CardSearch(cards.getAll())
      .ofOnlyAnyOfTheColors(deckColors)
      .notOfType(Type.CREATURE)
      .getCards();
    Collections.shuffle(nonCreatureCardsOfTheseColors);
    selectedCards.addAll(nonCreatureCardsOfTheseColors.subList(0, 3));

    return selectedCards;
  }

  private Card getRandomNonBasicLandOfTheseColors(Set<Color> deckColors) {
    List<Card> nonBasicLands = new CardSearch(cards.getAll())
      .ofType(Type.LAND)
      .notOfType(Type.BASIC)
      .getCards();

    List<Card> nonBasicLandsMatchingBothColors = nonBasicLands.stream()
      .filter(card -> matchesColors(card, deckColors))
      .collect(Collectors.toList());

    if (!nonBasicLandsMatchingBothColors.isEmpty()) {
      Collections.shuffle(nonBasicLandsMatchingBothColors);
      return nonBasicLandsMatchingBothColors.get(0);

    } else {
      List<Card> nonBasicLandsMatchingOneColor = nonBasicLands.stream()
        .filter(card -> matchesOneColor(card, deckColors))
        .collect(Collectors.toList());

      Collections.shuffle(nonBasicLandsMatchingOneColor);
      return nonBasicLandsMatchingOneColor.get(0);
    }
  }

  private Card getRandomColorlessCard() {
    List<Card> allColorlessCards = new CardSearch(cards.getAll())
      .colorless()
      .getCards();
    Collections.shuffle(allColorlessCards);

    return allColorlessCards.get(0);
  }

  private boolean matchesColors(Card card, Set<Color> deckColors) {
    Set<Color> cardColors = colorsOfManaThatCanGenerate(card);
    return deckColors.containsAll(cardColors);
  }

  private boolean matchesOneColor(Card card, Set<Color> deckColors) {
    Set<Color> cardColors = colorsOfManaThatCanGenerate(card);
    for (Color deckColor : deckColors) {
      for (Color cardColor : cardColors) {
        if (deckColor == cardColor) {
          return true;
        }
      }
    }

    return false;
  }

  private Set<Color> colorsOfManaThatCanGenerate(Card card) {
    return card.getAbilities().stream()
      .filter(ability -> ability.getAbilityType().equals(TAP_ADD_MANA))
      .flatMap(ability -> ability.getParameters().stream())
      .filter(cost -> !cost.equals("COLORLESS"))
      .map(Color::valueOf)
      .collect(Collectors.toSet());
  }
}
