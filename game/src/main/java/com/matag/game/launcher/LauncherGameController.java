package com.matag.game.launcher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.UUID;

@Controller
public class LauncherGameController {
  @ResponseBody
  @RequestMapping(path = {"/ui/game/{id}"}, method = RequestMethod.POST)
  public String launchGame(@PathVariable("id") Long id, HttpServletRequest httpServletRequest, Map<String, Object> model) {
    String session = httpServletRequest.getParameter("session");
    validateSessionFormat(session);

    return "<html>\n" +
      "    <head>\n" +
      "        <meta charset=\"utf-8\">\n" +
      "        <title>MATAG - Game</title>\n" +
      "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
      "        <link rel=\"shortcut icon\" type=\"image/png\" href=\"/img/matag.png\"/>\n" +
      "        <script>sessionStorage.setItem('token', '" + session + "')</script>" +
      "    </head>\n" +
      "    <body>\n" +
      "        <div id=\"app\" ></div>\n" +
      "        <script src=\"/js/game.js\" type=\"text/javascript\"></script>\n" +
      "    </body>\n" +
      "</html>";
  }

  private void validateSessionFormat(String session) {
    UUID.fromString(session);
  }
}
