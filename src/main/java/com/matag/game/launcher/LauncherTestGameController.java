package com.matag.game.launcher;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.AllArgsConstructor;

@Profile("test")
@Controller
@AllArgsConstructor
public class LauncherTestGameController {
  public static final AtomicLong TEST_ADMIN_TOKEN = new AtomicLong();
  private final LauncherGameResponseBuilder launcherGameResponseBuilder;

  @ResponseBody
  @GetMapping(path = {"/ui/test-game"})
  public String launchGame() {
    return launcherGameResponseBuilder.build(String.valueOf(TEST_ADMIN_TOKEN.incrementAndGet()));
  }
}
