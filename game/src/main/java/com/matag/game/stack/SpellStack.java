package com.matag.game.stack;

import com.matag.game.cardinstance.CardInstance;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
@Scope("prototype")
public class SpellStack {
  private LinkedList<CardInstance> items = new LinkedList<>();

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public void add(CardInstance cardInstance) {
    items.addLast(cardInstance);
  }

  public CardInstance peek() {
    return items.peekLast();
  }

  public CardInstance remove() {
    return items.removeLast();
  }

  public LinkedList<CardInstance> getItems() {
    return items;
  }

  public void setItems(LinkedList<CardInstance> items) {
    this.items = items;
  }
}
