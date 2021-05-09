package com.matag.game.stack;

import java.util.LinkedList;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.matag.game.cardinstance.CardInstance;
import com.matag.game.cardinstance.CardInstanceSearch;

@Component
@Scope("prototype")
public class SpellStack {
  private LinkedList<CardInstance> items = new LinkedList<>();

  public boolean isEmpty() {
    return items.isEmpty();
  }

  public void push(CardInstance cardInstance) {
    items.addLast(cardInstance);
  }

  public CardInstance peek() {
    return items.peekLast();
  }

  public CardInstance pop() {
    return items.removeLast();
  }

  public void remove(CardInstance card) {
    items.remove(card);  
  }

  public LinkedList<CardInstance> getItems() {
    return items;
  }

  public void setItems(LinkedList<CardInstance> items) {
    this.items = items;
  }

  public CardInstanceSearch search() {
    return new CardInstanceSearch(items);
  }
}
