package com.matag.game.player.playerInfo;

import com.matag.game.security.SecurityToken;
import org.springframework.stereotype.Component;

@Component
public class PlayerInfoRetriever {
  public PlayerInfo retrieve(SecurityToken token) {
    return new PlayerInfo("Guest");
  }
}
