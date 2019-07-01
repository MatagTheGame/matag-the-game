package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;

import java.util.List;

public interface MtgSet {

    String getName();

    String getCode();

    List<Card> getCards();

}
