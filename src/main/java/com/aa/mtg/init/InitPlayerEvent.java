package com.aa.mtg.init;

import com.aa.mtg.cards.Card;
import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.player.Player;
import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
class InitPlayerEvent {
    private List<CardInstance> library;
    private List<CardInstance> hand;
    private List<CardInstance> battlefield;
    private String name;
    private int life;

    static InitPlayerEvent createForPlayer(Player player) {
        List<CardInstance> hand = new ArrayList<>();
        for (CardInstance cardInstance : player.getHand().getCards()) {
            hand.add(new CardInstance(cardInstance));
        }

        return new InitPlayerEvent(
                fillLibrary(player),
                hand,
                new ArrayList<>(),
                player.getName(),
                player.getLife()
        );
    }

    static InitPlayerEvent createForOpponent(Player player) {
        List<CardInstance> hand = new ArrayList<>();
        for (CardInstance cardInstance : player.getHand().getCards()) {
            hand.add(new CardInstance(cardInstance.getId(), Card.hiddenCard()));
        }

        return new InitPlayerEvent(
                fillLibrary(player),
                hand,
                new ArrayList<>(),
                player.getName(),
                player.getLife()
        );
    }

    private static List<CardInstance> fillLibrary(Player player) {
        List<CardInstance> library = new ArrayList<>();
        for (CardInstance cardInstance : player.getLibrary().getCards()) {
            library.add(new CardInstance(cardInstance.getId(), Card.hiddenCard()));
        }
        return library;
    }
}
