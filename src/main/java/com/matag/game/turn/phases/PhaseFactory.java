package com.matag.game.turn.phases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PhaseFactory {
  @Autowired
  private List<Phase> phases;

  public Phase get(String phaseName) {
    return phases.stream()
      .filter(phase -> phase.getName().equals(phaseName))
      .findFirst()
      .orElseThrow(() -> new UnsupportedOperationException("Phase " + phaseName + " not valid"));
  }
}
