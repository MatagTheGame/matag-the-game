package com.matag.game.launcher;

import org.springframework.stereotype.Component;

@Component
public class LauncherGameResponseBuilder {
  public String build(String session) {
    return
      "<html>\n" +
      "    <head>\n" +
      "        <meta charset=\"utf-8\">\n" +
      "        <title>MATAG - Game</title>\n" +
      "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
      "        <link rel=\"shortcut icon\" type=\"image/png\" href=\"/img/matag.png\"/>\n" +
      "        <script>sessionStorage.setItem('token', '" + session + "')</script>\n" +
      "    </head>\n" +
      "    <body>\n" +
      "        <div id=\"app\" ></div>\n" +
      "        <script src=\"/js/game.js\" type=\"text/javascript\"></script>\n" +
      "    </body>\n" +
      "</html>";
  }
}
