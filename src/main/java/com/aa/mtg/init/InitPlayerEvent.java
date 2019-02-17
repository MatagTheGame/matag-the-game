package com.aa.mtg.init;

import com.aa.mtg.cards.model.Card;
import com.aa.mtg.cards.model.CardInstance;
import com.aa.mtg.cards.model.HiddenCard;
import com.aa.mtg.player.Player;

import java.util.ArrayList;
import java.util.List;

class InitPlayerEvent {
    private final List<CardInstance> library;
    private final List<CardInstance> hand;
    private final List<CardInstance> battlefield;
    private final String name;
    private final int life;

    public InitPlayerEvent(List<CardInstance> library, List<CardInstance> hand, List<CardInstance> battlefield, String name, int life) {
        this.library = library;
        this.hand = hand;
        this.battlefield = battlefield;
        this.name = name;
        this.life = life;
    }

    public List<CardInstance> getLibrary() {
        return library;
    }

    public List<CardInstance> getHand() {
        return hand;
    }

    public List<CardInstance> getBattlefield() {
        return battlefield;
    }

    public String getName() {
        return name;
    }

    public int getLife() {
        return life;
    }

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
            hand.add(new CardInstance(cardInstance.getId(), new HiddenCard()));
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
            library.add(new CardInstance(cardInstance.getId(), new HiddenCard()));
        }
        return library;
    }
}
