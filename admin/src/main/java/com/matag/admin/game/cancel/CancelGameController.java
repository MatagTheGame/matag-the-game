package com.matag.admin.game.cancel;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class CancelGameController {
  private final CancelGameService cancelGameService;

  @DeleteMapping("/{id}")
  public void joinGame(@PathVariable("id") Long gameId) {
    cancelGameService.cancel(gameId);
  }
}
