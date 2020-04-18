package com.matag.admin.game.deck;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionRepository;
import com.matag.adminentities.DeckInfo;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/game/active-deck")
public class DeckRetrieverController {
  private final GameSessionRepository gameSessionRepository;
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final ObjectMapper objectMapper;

  @GetMapping
  public DeckInfo deckInfo() {
    String sessionId = securityContextHolderHelper.getSession().getId();
    return gameSessionRepository.findPlayerActiveGameSession(sessionId)
      .map(this::readDeckInfo)
      .orElseThrow(() -> new RuntimeException("Active deck not found."));
  }

  @SneakyThrows
  private DeckInfo readDeckInfo(GameSession gs) {
    return objectMapper.readValue(gs.getPlayerOptions(), DeckInfo.class);
  }
}
