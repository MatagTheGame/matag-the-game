package com.matag.admin.game;

import com.matag.admin.game.session.GameSessionRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class GameController {
  private final GameRepository gameRepository;
  private final GameSessionRepository gameSessionRepository;

  @PostMapping
  public long joinGame(@RequestBody JoinGameRequest joinGameRequest) {
    Game game = Game.builder()
      .type(joinGameRequest.getGameType())
      .status(GameStatusType.STARTING)
      .build();

    gameRepository.save(game);

    return game.getId();
  }
}
