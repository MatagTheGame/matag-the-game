package com.matag.cards.sets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matag.cards.CardsConfiguration;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class MtgSets {
  private static final String SETS_PATH = CardsConfiguration.getResourcesPath() + "/sets";

  private Map<String, MtgSet> SETS = new LinkedHashMap<>();

  public MtgSets(ObjectMapper objectMapper) throws Exception {
    String[] sets = new File(SETS_PATH).list();
    Objects.requireNonNull(sets);
    for (String set : sets) {
      MtgSet mtgSet = objectMapper.readValue(new File(SETS_PATH + "/" + set), MtgSet.class);
      SETS.put(mtgSet.getCode(), mtgSet);
    }
  }

  public Map<String, MtgSet> getSets() {
    return SETS;
  }

  public MtgSet getSet(String code) {
    return SETS.get(code);
  }

  public int countCards() {
    return SETS.values().stream()
      .map(set -> set.getCards().size())
      .reduce(Integer::sum)
      .orElse(0);
  }
}
