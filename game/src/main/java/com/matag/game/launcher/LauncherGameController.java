package com.matag.game.launcher;

import com.matag.game.config.ConfigService;
import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class LauncherGameController {
  private final LauncherGameResponseBuilder launcherGameResponseBuilder;
  private final ConfigService configService;
  private final Environment env;

  @ResponseBody
  @PostMapping(path = {"/ui/game/{id}"})
  public String launchGame(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
    String session = httpServletRequest.getParameter("session");
    validateSessionFormat(session);

    return launcherGameResponseBuilder.build(session);
  }

  @GetMapping(path = {"/ui/game/{id}"})
  public RedirectView onRefresh() {
    return new RedirectView( configService.getMatagAdminUrl() + "/ui/admin/play");
  }

  private void validateSessionFormat(String session) {
    UUID.fromString(session);
  }

  private String[] isTestProfile() {
    return env.getActiveProfiles();
  }
}
