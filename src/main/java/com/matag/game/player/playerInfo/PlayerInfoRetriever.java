package com.matag.game.player.playerInfo;

import org.springframework.stereotype.Component;

import com.matag.adminentities.PlayerInfo;
import com.matag.game.adminclient.AdminClient;
import com.matag.game.security.SecurityToken;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class PlayerInfoRetriever {
  private final AdminClient adminClient;

  public PlayerInfo retrieve(SecurityToken token) {
    return adminClient.getPlayerInfo(token);
  }
}
