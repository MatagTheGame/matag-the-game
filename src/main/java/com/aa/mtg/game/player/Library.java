package com.aa.mtg.game.player;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.CardInstanceFactory;
import com.aa.mtg.cards.CardListComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class Library extends CardListComponent {
    private final CardInstanceFactory cardInstanceFactory;

    public Library(CardInstanceFactory cardInstanceFactory) {
        this.cardInstanceFactory = cardInstanceFactory;
    }

    public CardInstance draw() {
        return this.cards.remove(0);
    }

    public List<CardInstance> maskedLibrary() {
        return cardInstanceFactory.mask(this.cards);
    }

    public Library shuffle() {
        Collections.shuffle(cards);
        return this;
    }
}
