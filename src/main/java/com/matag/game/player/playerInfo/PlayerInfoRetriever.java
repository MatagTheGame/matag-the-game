package com.matag.game.player.playerInfo;

import com.matag.adminentities.PlayerInfo;
import com.matag.game.adminclient.AdminClient;
import com.matag.game.security.SecurityToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class PlayerInfoRetriever {
  private final AdminClient adminClient;

  public PlayerInfo retrieve(SecurityToken token) {
    return adminClient.getPlayerInfo(token);
  }
}
