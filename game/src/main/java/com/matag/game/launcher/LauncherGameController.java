package com.matag.game.launcher;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LauncherGameController {
  @Deprecated
  @RequestMapping(path = {"/ui/game/{id}"}, method = RequestMethod.GET)
  public String launchGameOld(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
    System.out.println("ciao");
    return "forward:/game.html";
  }

  @RequestMapping(path = {"/ui/game/{id}"}, method = RequestMethod.POST)
  public String launchGame(@PathVariable("id") Long id, HttpServletRequest httpServletRequest) {
    String session = httpServletRequest.getParameter("session");
    return "forward:/game.html";
  }
}
