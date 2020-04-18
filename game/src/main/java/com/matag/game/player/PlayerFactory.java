package com.matag.game.player;

import com.matag.adminentities.PlayerInfo;
import com.matag.game.security.SecurityToken;
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

  public Player create(SecurityToken token, PlayerInfo playerInfo) {
    Player player = applicationContext.getBean(Player.class);
    player.setToken(token);
    player.setName(playerInfo.getPlayerName());
    player.setResolution("lowResolution");
    return player;
  }
}
