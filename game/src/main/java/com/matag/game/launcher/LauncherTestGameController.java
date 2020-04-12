package com.matag.game.launcher;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.UUID;

@Profile("test")
@Controller
@AllArgsConstructor
public class LauncherTestGameController {
  private final LauncherGameResponseBuilder launcherGameResponseBuilder;

  @ResponseBody
  @RequestMapping(path = {"/ui/test-game/{id}"}, method = RequestMethod.GET)
  public String launchGame(@PathVariable("id") Long id) {
    return launcherGameResponseBuilder.build(UUID.randomUUID().toString());
  }
}
