package com.matag.game.deck;

import com.matag.game.security.SecurityToken;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.matag.cards.properties.Color.RED;
import static com.matag.cards.properties.Color.WHITE;

@Component
@AllArgsConstructor
public class DeckRetrieverService {
  public DeckInfo retrieveDeckForUser(SecurityToken token) {
    return new DeckInfo(Set.of(WHITE, RED));
  }
}
