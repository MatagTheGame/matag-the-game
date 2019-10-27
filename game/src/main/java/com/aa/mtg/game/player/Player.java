package com.aa.mtg.game.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
@Scope("prototype")
public class Player {

    private String sessionId;
    private String name;
    private int life;
    private final Library library;
    private final Hand hand;
    private final Battlefield battlefield;
    private final Graveyard graveyard;

    @Autowired
    public Player(Library library, Hand hand, Battlefield battlefield, Graveyard graveyard) {
        this.life = 20;
        this.library = library;
        this.hand = hand;
        this.battlefield = battlefield;
        this.graveyard = graveyard;
    }

    public void drawHand() {
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

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
