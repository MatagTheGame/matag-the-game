package com.aa.mtg.cards.sets;

import static com.aa.mtg.cards.ability.Abilities.PAY_1_EQUIP_CREATURE_GETS_PLUS_1_0_AND_HASTE;
import static com.aa.mtg.cards.properties.Rarity.COMMON;
import static com.aa.mtg.cards.properties.Subtype.EQUIPMENT;
import static com.aa.mtg.cards.properties.Type.ARTIFACT;
import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.properties.Color;
import com.aa.mtg.cards.properties.Cost;
import java.util.ArrayList;
import java.util.List;

public class ThroneOfEldraine implements MtgSet {

    public static final String ELD = "ELD";

    public static Card CRYSTAL_SLIPPER = new Card("Crystal Slipper", singleton(Color.RED), asList(Cost.RED, Cost.COLORLESS), singletonList(ARTIFACT), singletonList(EQUIPMENT), COMMON, "Equipped creature gets +1/+0 and has haste. Equip 1", 0, 0, singletonList(PAY_1_EQUIP_CREATURE_GETS_PLUS_1_0_AND_HASTE));

    private static ThroneOfEldraine instance;

    private List<Card> cards = new ArrayList<>();

    private ThroneOfEldraine() {
        cards.add(CRYSTAL_SLIPPER);
    }

    @Override
    public String getName() {
        return "Throne of Eldraine";
    }

    @Override
    public String getCode() {
        return ELD;
    }

    @Override
    public List<Card> getCards() {
        return cards;
    }

    public static ThroneOfEldraine throneOfEldraine() {
        if (instance == null) {
            instance = new ThroneOfEldraine();
        }
        return instance;
    }
}
