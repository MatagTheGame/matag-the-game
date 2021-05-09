package com.matag.game.player;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardListComponent;

@Component
@Scope("prototype")
public class Library extends CardListComponent {
  public List<CardInstance> peek(int n) {
    return this.cards.subList(0, n);
  }

  public CardInstance draw() {
    return this.cards.remove(0);
  }

  public Library shuffle() {
    Collections.shuffle(cards);
    return this;
  }
}
