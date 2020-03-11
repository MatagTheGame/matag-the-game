package com.matag.game.turn;

import com.matag.game.status.GameStatus;
import com.matag.game.turn.action._continue.ContinueTurnService;
import com.matag.game.turn.action._continue.ResolveService;
import com.matag.game.turn.action.cast.CastService;
import com.matag.game.turn.action.cast.PlayLandService;
import com.matag.game.turn.action.combat.DeclareAttackerService;
import com.matag.game.turn.action.combat.DeclareBlockerService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TurnService {

  private final ContinueTurnService continueTurnService;
  private final PlayLandService playLandService;
  private final CastService castService;
  private final ResolveService resolveService;
  private final DeclareAttackerService declareAttackerService;
  private final DeclareBlockerService declareBlockerService;

  public TurnService(ContinueTurnService continueTurnService, PlayLandService playLandService, CastService castService, ResolveService resolveService, DeclareAttackerService declareAttackerService, DeclareBlockerService declareBlockerService) {
    this.continueTurnService = continueTurnService;
    this.playLandService = playLandService;
    this.castService = castService;
    this.resolveService = resolveService;
    this.declareAttackerService = declareAttackerService;
    this.declareBlockerService = declareBlockerService;
  }

  void continueTurn(GameStatus gameStatus) {
    continueTurnService.continueTurn(gameStatus);
  }

  void playLand(GameStatus gameStatus, int cardId) {
    playLandService.playLand(gameStatus, cardId);
  }

  void cast(GameStatus gameStatus, int cardId, Map<Integer, List<String>> mana, Map<Integer, List<Object>> targetsIdsForCardIds, String playedAbility) {
    castService.cast(gameStatus, cardId, mana, targetsIdsForCardIds, playedAbility);
  }

  void resolve(GameStatus gameStatus, String triggeredNonStackAction, List<Integer> targetCardIds, Map<Integer, List<Object>> targetsIdsForCardIds) {
    resolveService.resolve(gameStatus, triggeredNonStackAction, targetCardIds, targetsIdsForCardIds);
  }

  void declareAttackers(GameStatus gameStatus, List<Integer> cardIds) {
    declareAttackerService.declareAttackers(gameStatus, cardIds);
  }

  void declareBlockers(GameStatus gameStatus, Map<Integer, List<Integer>> targetsIdsForCardIds) {
    declareBlockerService.declareBlockers(gameStatus, targetsIdsForCardIds);
  }
}
