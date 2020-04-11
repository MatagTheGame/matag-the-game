package com.matag.game.launcher;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Profile("test")
@Controller
@AllArgsConstructor
public class LauncherTestGameController {
  public static final String PLAYER_1_SESSION_TOKEN = "00000000-0000-0000-0000-000000000001";
  public static final String PLAYER_2_SESSION_TOKEN = "00000000-0000-0000-0000-000000000002";

  private final LauncherGameResponseBuilder launcherGameResponseBuilder;

  @ResponseBody
  @RequestMapping(path = {"/ui/game/{id}"}, method = RequestMethod.GET)
  public String launchGame(@PathVariable("id") Long id) {
    return launcherGameResponseBuilder.build(PLAYER_1_SESSION_TOKEN);
  }
}
