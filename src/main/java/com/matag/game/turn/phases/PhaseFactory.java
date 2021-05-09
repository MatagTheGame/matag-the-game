package com.matag.game.turn.phases;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

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
