package com.matag.admin.game.player;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.session.MatagSession;
import com.matag.adminentities.PlayerInfo;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@AllArgsConstructor
@RestController
@RequestMapping("/player/info")
public class PlayerInfoRetrieverController {
  private static final AtomicInteger GUEST_COUNTER = new AtomicInteger();
  private final SecurityContextHolderHelper securityContextHolderHelper;

  @GetMapping
  public PlayerInfo deckInfo() {
    MatagSession session = securityContextHolderHelper.getSession();
    return new PlayerInfo(session.getMatagUser().getUsername() + "-" + GUEST_COUNTER.incrementAndGet());
  }
}
