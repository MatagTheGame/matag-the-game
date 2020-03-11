package com.matag.game.player;

import com.matag.cardinstance.CardInstance;
import com.matag.cardinstance.CardInstanceFactory;
import com.matag.cardinstance.CardListComponent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@Scope("prototype")
public class Library extends CardListComponent {
  private final CardInstanceFactory cardInstanceFactory;

  public Library(CardInstanceFactory cardInstanceFactory) {
    this.cardInstanceFactory = cardInstanceFactory;
  }

  public CardInstance draw() {
    return this.cards.remove(0);
  }

  public List<CardInstance> maskedLibrary() {
    return cardInstanceFactory.mask(this.cards);
  }

  public Library shuffle() {
    Collections.shuffle(cards);
    return this;
  }
}
