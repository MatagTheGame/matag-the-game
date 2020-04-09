package com.matag.game.start;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.concurrent.atomic.AtomicInteger;

@Controller
public class StartGameController {
  private AtomicInteger atomicInteger = new AtomicInteger(1);

  @Deprecated
  @RequestMapping(path = {"/ui", "/ui/game"}, method = RequestMethod.GET)
  public String startGameGet() {
    return "redirect:/ui/game/" + atomicInteger.incrementAndGet() / 2;
  }

  @RequestMapping(path = {"/ui", "/ui/game"}, method = RequestMethod.POST)
  public String startGame() {
    return "redirect:/ui/game/" + atomicInteger.incrementAndGet() / 2;
  }

}
