package com.aa.mtg.game.player;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class PlayerFactory implements ApplicationContextAware {
  private ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }

  public Player create(String sessionId, String name) {
    Player player = applicationContext.getBean(Player.class);
    player.setSessionId(sessionId);
    player.setName(name);
    player.setResolution("lowResolution");
    return player;
  }
}
