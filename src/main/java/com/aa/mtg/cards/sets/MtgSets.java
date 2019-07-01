package com.aa.mtg.cards.sets;

import java.util.ArrayList;
import java.util.List;

import static com.aa.mtg.cards.sets.GuildsOfRavnica.guildsOfRavnica;
import static com.aa.mtg.cards.sets.Ixalan.ixalan;
import static com.aa.mtg.cards.sets.RavnicaAllegiance.ravnicaAllegiance;
import static com.aa.mtg.cards.sets.WarOfTheSpark.warOfTheSpark;

public class MtgSets {

    private static MtgSets instance;

    private static List<MtgSet> MTG_SETS = new ArrayList<>();

    private MtgSets() {
        MTG_SETS.add(warOfTheSpark());
        MTG_SETS.add(ravnicaAllegiance());
        MTG_SETS.add(guildsOfRavnica());
        MTG_SETS.add(ixalan());
    }

    public static MtgSets getInstance() {
        return instance;
    }

    public MtgSets mtgSets() {
        if (instance == null) {
            instance = new MtgSets();
        }
        return mtgSets();
    }
}
