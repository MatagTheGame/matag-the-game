package com.matag.game.turn.phases;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@Component
public class PhaseFactory {
  private final List<Phase> phases;

  public Phase get(String phaseName) {
    return phases.stream()
      .filter(phase -> phase.getName().equals(phaseName))
      .findFirst()
      .orElseThrow(() -> new UnsupportedOperationException("Phase " + phaseName + " not valid"));
  }
}
