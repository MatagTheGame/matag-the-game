package com.aa.mtg.game.deck;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.security.SecurityToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.aa.mtg.cards.Cards.FOREST;
import static com.aa.mtg.cards.Cards.MOUNTAIN;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.AXEBANE_BEAST;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.FERAL_MAAKA;

@Component
public class DeckRetrieverService {

    public Library retrieveDeckForUser(SecurityToken token, GameStatus gameStatus) {
        List<CardInstance> libraryCards = IntStream.rangeClosed(1, 60)
                .boxed()
                .map(i -> new CardInstance(gameStatus.nextCardId(), getRandomCard()))
                .collect(Collectors.toList());


        return new Library(libraryCards);
    }

    private Card getRandomCard() {
        switch (new Random().nextInt(4)) {
            case 0: return MOUNTAIN;
            case 1: return FOREST;
            case 2: return FERAL_MAAKA;
            case 3: return AXEBANE_BEAST;
        }
        throw new RuntimeException("Non Existent Card");
    }

}
