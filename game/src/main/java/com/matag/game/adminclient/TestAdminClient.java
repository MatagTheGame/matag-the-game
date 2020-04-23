package com.matag.game.adminclient;

import com.matag.adminentities.DeckInfo;
import com.matag.adminentities.PlayerInfo;
import com.matag.game.security.SecurityToken;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static com.matag.cards.properties.Color.WHITE;

@Profile("test")
@Component
public class TestAdminClient extends AdminClient {
  public static AtomicInteger GUEST_COUNTER = new AtomicInteger();

  public TestAdminClient() {
    super(null);
  }

  public DeckInfo getDeckInfo(SecurityToken token) {
    return new DeckInfo(Set.of(WHITE));
  }

  public PlayerInfo getPlayerInfo(SecurityToken token) {
    return new PlayerInfo("Guest-" + ((GUEST_COUNTER.incrementAndGet() / 2) + 1));
  }
}
