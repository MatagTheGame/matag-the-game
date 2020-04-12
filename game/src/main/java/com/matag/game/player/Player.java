package com.matag.game.player;

import com.matag.game.security.SecurityToken;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Data
@Component
@Scope("prototype")
public class Player {
  private SecurityToken token;
  private String name;
  private int life;
  private final Library library;
  private final Hand hand;
  private final Battlefield battlefield;
  private final Graveyard graveyard;
  private String resolution;

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

}
