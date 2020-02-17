package com.aa.mtg.game.init;

import com.aa.mtg.cardinstance.CardInstance;
import com.aa.mtg.game.player.Player;

import java.util.List;

class InitPlayerEvent {
    private final int librarySize;
    private final List<CardInstance> hand;
    private final List<CardInstance> battlefield;
    private final List<CardInstance> graveyard;
    private final String name;
    private final int life;

    public InitPlayerEvent(int librarySize, List<CardInstance> hand, List<CardInstance> battlefield, List<CardInstance> graveyard, String name, int life) {
        this.librarySize = librarySize;
        this.hand = hand;
        this.battlefield = battlefield;
        this.graveyard = graveyard;
        this.name = name;
        this.life = life;
    }

    public int getLibrarySize() {
        return librarySize;
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
                player.getLibrary().size(),
                player.getHand().getCards(),
                player.getBattlefield().getCards(),
                player.getGraveyard().getCards(),
                player.getName(),
                player.getLife()
        );
    }

    static InitPlayerEvent createForOpponent(Player player) {
        return new InitPlayerEvent(
                player.getLibrary().size(),
                player.getHand().maskedHand(),
                player.getBattlefield().getCards(),
                player.getGraveyard().getCards(),
                player.getName(),
                player.getLife()
        );
    }
}
