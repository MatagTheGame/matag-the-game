package com.aa.mtg.game.player;

import java.util.stream.IntStream;

public class Player {

    private final String sessionId;
    private final String name;
    private int life;
    private final Library library;
    private final Hand hand;
    private final Battlefield battlefield;
    private final Graveyard graveyard;

    public Player(String sessionId, String name, Library library) {
        this.sessionId = sessionId;
        this.name = name;
        this.life = 20;
        this.library = library;
        this.hand = new Hand();
        this.battlefield = new Battlefield();
        this.graveyard = new Graveyard();

        IntStream.rangeClosed(1, 7)
                .forEach(i -> this.hand.addCard(this.library.draw()));
    }

    public void increaseLife(int life) {
        this.life += life;
    }

    public void decreaseLife(int life) {
        this.life -= life;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }

    public int getLife() {
        return life;
    }

    public Library getLibrary() {
        return library;
    }

    public Hand getHand() {
        return hand;
    }

    public Battlefield getBattlefield() {
        return battlefield;
    }

    public Graveyard getGraveyard() {
        return graveyard;
    }
}
