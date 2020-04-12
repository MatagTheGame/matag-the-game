package com.matag.game.deck;

import com.matag.cards.Card;
import com.matag.cards.CardUtils;
import com.matag.cards.Cards;
import com.matag.cards.properties.Color;
import com.matag.cards.properties.Type;
import com.matag.cards.search.CardSearch;
import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceFactory;
import com.matag.game.status.GameStatus;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.nCopies;

@Component
@AllArgsConstructor
public class DeckFactory {
  private final CardInstanceFactory cardInstanceFactory;
  private final Cards cards;

  public List<CardInstance> create(String playerName, GameStatus gameStatus, DeckInfo deckInfo) {
    List<Card> cards;

    if (deckInfo.getRandomColors() != null) {
      cards = randomColorsCards(deckInfo.getRandomColors());

    } else {
      throw new UnsupportedOperationException();
    }

    return cards.stream()
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
      .ofAnyOfTheColors(deckColors)
      .ofType(Type.CREATURE)
      .getCards();
    Collections.shuffle(creatureCardsOfTheseColors);
    selectedCards.addAll(creatureCardsOfTheseColors.subList(0, 5));

    List<Card> nonCreatureCardsOfTheseColors = new CardSearch(cards.getAll())
      .ofAnyOfTheColors(deckColors)
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
      .getCards()
      .stream()
      .filter(card -> CardUtils.colorsOfManaThatCanGenerate(card).size() == 0 || deckColors.contains(CardUtils.colorsOfManaThatCanGenerate(card).get(0)))
      .collect(Collectors.toList());
    Collections.shuffle(nonBasicLands);
    return nonBasicLands.get(0);
  }

  private Card getRandomColorlessCard() {
    List<Card> allColorlessCards = new CardSearch(cards.getAll())
      .colorless()
      .getCards();
    Collections.shuffle(allColorlessCards);

    return allColorlessCards.get(0);
  }
}
