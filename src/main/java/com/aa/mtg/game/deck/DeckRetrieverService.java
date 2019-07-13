package com.aa.mtg.game.deck;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.search.CardSearch;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.security.SecurityToken;
import com.aa.mtg.game.status.GameStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.aa.mtg.cards.Cards.*;
import static com.aa.mtg.cards.properties.Color.*;
import static com.aa.mtg.cards.sets.MtgSets.mtgSets;
import static java.util.Arrays.asList;

@Component
public class DeckRetrieverService {

    public Library retrieveDeckForUser(SecurityToken token, String playerName, GameStatus gameStatus) {
        return randomDeck(playerName, gameStatus);
    }

    private Library randomDeck(String playerName, GameStatus gameStatus) {
        List<CardInstance> cards = new ArrayList<>();

        List<Color> deckColors = randomTwoColors();
        List<Card> randomCardsOfTheseColors = getRandomCardsForColors(deckColors);

        for (Color color : deckColors) {
            addNCards(gameStatus, cards, playerName, 12, getBasicLandForColor(color));
        }

        for (Card randomCard : randomCardsOfTheseColors) {
            addNCards(gameStatus, cards, playerName, 4, randomCard);
        }

        addNCards(gameStatus, cards, playerName, 4, getRandomColorlessCard());

        Collections.shuffle(cards);
        return new Library(cards);
    }

    private Card getBasicLandForColor(Color color) {
        switch (color) {
            case WHITE:
                return PLAINS;
            case BLUE:
                return ISLAND;
            case BLACK:
                return SWAMP;
            case RED:
                return MOUNTAIN;
            case GREEN:
                return FOREST;
            default:
                throw new RuntimeException("Basic Land for color " + color + " does not exist.");
        }
    }

    private List<Color> randomTwoColors() {
        List<Color> colors = asList(WHITE, BLUE, BLACK, RED, GREEN);
        Collections.shuffle(colors);
        return colors.subList(0, 2);
    }

    private List<Card> getRandomCardsForColors(List<Color> deckColors) {
        List<Card> allCardsOfTheseColors = new CardSearch(mtgSets().getAllCards())
                .ofAnyOfTheColors(deckColors)
                .getCards();
        Collections.shuffle(allCardsOfTheseColors);

        return allCardsOfTheseColors.subList(0, 8);
    }

    private Card getRandomColorlessCard() {
        List<Card> allColorlessCards = new CardSearch(mtgSets().getAllCards())
                .colorless()
                .getCards();
        Collections.shuffle(allColorlessCards);

        return allColorlessCards.get(0);
    }

    private void addNCards(GameStatus gameStatus, List<CardInstance> cards, String playerName, int n, Card plains) {
        for (int i = 0; i < n; i++) {
            cards.add(new CardInstance(gameStatus, gameStatus.nextCardId(), plains, playerName));
        }
    }
}
