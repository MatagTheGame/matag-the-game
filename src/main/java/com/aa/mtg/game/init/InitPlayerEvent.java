package com.aa.mtg.game.init;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.game.player.Player;

import java.util.ArrayList;
import java.util.List;

class InitPlayerEvent {
    private final List<CardInstance> library;
    private final List<CardInstance> hand;
    private final List<CardInstance> battlefield;
    private final List<CardInstance> graveyard;
    private final String name;
    private final int life;

    public InitPlayerEvent(List<CardInstance> library, List<CardInstance> hand, List<CardInstance> battlefield, List<CardInstance> graveyard, String name, int life) {
        this.library = library;
        this.hand = hand;
        this.battlefield = battlefield;
        this.graveyard = graveyard;
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

    public List<CardInstance> getGraveyard() {
        return graveyard;
    }

    public String getName() {
        return name;
    }

    public int getLife() {
        return life;
    }

    static InitPlayerEvent createForPlayer(Player player) {
        return new InitPlayerEvent(
                player.getLibrary().maskedLibrary(),
                player.getHand().getCards(),
                new ArrayList<>(),
                new ArrayList<>(),
                player.getName(),
                player.getLife()
        );
    }

    static InitPlayerEvent createForOpponent(Player player) {
        return new InitPlayerEvent(
                player.getLibrary().maskedLibrary(),
                player.getHand().maskedHand(),
                new ArrayList<>(),
                new ArrayList<>(),
                player.getName(),
                player.getLife()
        );
    }
}
