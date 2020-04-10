package com.matag.admin.game;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class GameController {
  private final GameService gameService;

  @PostMapping
  public JoinGameResponse joinGame(@RequestBody JoinGameRequest joinGameRequest) {
    return gameService.joinGame(joinGameRequest);
  }
}
