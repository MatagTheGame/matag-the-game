package com.aa.mtg.cards.sets;

import com.aa.mtg.cards.Card;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.sets.CoreSet2019.m19;
import static com.aa.mtg.cards.sets.CoreSet2020.m20;
import static com.aa.mtg.cards.sets.Dominaria.dominaria;
import static com.aa.mtg.cards.sets.GuildsOfRavnica.guildsOfRavnica;
import static com.aa.mtg.cards.sets.Ixalan.ixalan;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.ravnicaAllegiance;
import static com.aa.mtg.cards.sets.RivalsOfIxalan.rivalsOfIxalan;
import static com.aa.mtg.cards.sets.WarOfTheSpark.warOfTheSpark;

@Component
public class MtgSets {

    private List<MtgSet> MTG_SETS = new ArrayList<>();

    private MtgSets() {
        MTG_SETS.add(m20());
        MTG_SETS.add(warOfTheSpark());
        MTG_SETS.add(ravnicaAllegiance());
        MTG_SETS.add(guildsOfRavnica());
        MTG_SETS.add(m19());
        MTG_SETS.add(dominaria());
        MTG_SETS.add(rivalsOfIxalan());
        MTG_SETS.add(ixalan());
    }

    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        for (MtgSet mtgSet : MTG_SETS) {
            cards.addAll(mtgSet.getCards());
        }
        return cards;
    }

    public MtgSet getSetForCard(Card card) {
        for (MtgSet mtgSet : MTG_SETS) {
            if (mtgSet.getCards().contains(card)) {
                return mtgSet;
            }
        }

        throw new RuntimeException("Card " + card.getName() + " not found in any set.");
    }
}
