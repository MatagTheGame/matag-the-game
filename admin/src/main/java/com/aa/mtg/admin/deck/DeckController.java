package com.aa.mtg.admin.deck;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Collections.emptyList;

@RestController
@RequestMapping("/deck")
public class DeckController {

  @GetMapping()
  public List<Deck> decks() {
    return emptyList();
  }
}
