package com.matag.game.adminclient;

import com.matag.adminentities.DeckInfo;
import com.matag.adminentities.PlayerInfo;
import com.matag.game.config.ConfigService;
import com.matag.game.security.SecurityToken;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.matag.cards.properties.Color.WHITE;

@Profile("test")
@Component
public class TestAdminClient extends AdminClient {
  public static boolean playerJoined = false;

  public TestAdminClient(ConfigService configService) {
    super(configService);
  }

  public DeckInfo getDeckInfo(SecurityToken token) {
    return new DeckInfo(Set.of(WHITE));
  }

  public PlayerInfo getPlayerInfo(SecurityToken token) {
    if (!playerJoined) {
      playerJoined = true;
      return new PlayerInfo("Guest-1");

    } else {
      return new PlayerInfo("Guest-2");
    }
  }
}
