package com.matag.game.launcher;

import com.matag.game.config.ConfigService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import java.util.UUID;

@Controller
@AllArgsConstructor
public class LauncherGameController {
    private final LauncherGameResponseBuilder launcherGameResponseBuilder;
    private final ConfigService configService;

    @ResponseBody
    @PostMapping(path = {"/ui/{id}"})
    public String launchGame(HttpServletRequest httpServletRequest) {
        var session = httpServletRequest.getParameter("session");
        validateSessionFormat(session);

        return launcherGameResponseBuilder.build(session);
    }

    @GetMapping(path = {"/ui/{id}"})
    public RedirectView onRefresh() {
        return new RedirectView(configService.getMatagAdminPath() + "/play");
    }

    private void validateSessionFormat(String session) {
        UUID.fromString(session);
    }
}
