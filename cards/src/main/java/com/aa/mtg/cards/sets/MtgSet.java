package com.aa.mtg.cards.sets;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class MtgSet {
    private final String code;
    private final String name;
    private final List<String> cards;

    @JsonCreator
    public MtgSet(@JsonProperty("code") String code, @JsonProperty("name") String name, @JsonProperty("cards") List<String> cards) {
        this.code = code;
        this.name = name;
        this.cards = cards;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<String> getCards() {
        return cards;
    }
}
