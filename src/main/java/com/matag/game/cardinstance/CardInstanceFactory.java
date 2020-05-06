package com.matag.game.cardinstance;

import com.matag.cards.Card;
import com.matag.cards.CardUtils;
import com.matag.game.status.GameStatus;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CardInstanceFactory implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public CardInstance create(int id, Card card, String owner, String controller) {
    CardInstance cardInstance = new CardInstance();
    cardInstance.setId(id);
    cardInstance.setCard(card);
    cardInstance.setOwner(owner);
    cardInstance.setController(controller);
    return cardInstance;
  }

  public CardInstance create(int id, Card card, String owner) {
    return create(id, card, owner, null);
  }

  public CardInstance mask(CardInstance cardInstance) {
    return create(cardInstance.getId(), CardUtils.hiddenCard(), cardInstance.getOwner());
  }

  public List<CardInstance> mask(List<CardInstance> cardInstanceList) {
    return cardInstanceList.stream()
      .map(this::mask)
      .collect(Collectors.toList());
  }
}
