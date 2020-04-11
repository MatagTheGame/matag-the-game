package com.matag.game.launcher;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class LauncherGameController {
  private final LauncherGameResponseBuilder launcherGameResponseBuilder;

  @ResponseBody
  @RequestMapping(path = {"/ui/game/{id}"}, method = RequestMethod.POST)
  public String launchGame(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
    String session = httpServletRequest.getParameter("session");
    validateSessionFormat(session);

    return launcherGameResponseBuilder.build(session);
  }

  private void validateSessionFormat(String session) {
    UUID.fromString(session);
  }
}
