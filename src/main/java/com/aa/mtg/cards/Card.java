package com.aa.mtg.cards;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Card {

    private String name;
    private List<Color> color;
    private List<Cost> cost;
    private List<Type> type;
    private List<String> subtype;
    private String ruleText;
    private int power;
    private int toughness;

}
