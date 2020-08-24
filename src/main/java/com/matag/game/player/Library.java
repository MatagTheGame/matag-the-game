package com.matag.game.player;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardListComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@Scope("prototype")
public class Library extends CardListComponent {
  public CardInstance draw() {
    return this.cards.remove(0);
  }

  public Library shuffle() {
    Collections.shuffle(cards);
    return this;
  }
}
