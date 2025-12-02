package com.matag.adminentities;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.matag.cards.Card;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@JsonDeserialize(builder = DeckInfo.DeckInfoBuilder.class)
@Builder(toBuilder = true)
public class DeckInfo {
    List<Card> cards;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DeckInfoBuilder {

    }
}
