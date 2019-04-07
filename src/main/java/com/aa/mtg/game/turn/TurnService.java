package com.aa.mtg.game.turn;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.CastService;
import com.aa.mtg.game.turn.action.ContinueTurnService;
import com.aa.mtg.game.turn.action.DeclareAttackerService;
import com.aa.mtg.game.turn.action.DeclareBlockerService;
import com.aa.mtg.game.turn.action.PlayLandService;
import com.aa.mtg.game.turn.action.ResolveService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
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

    void cast(GameStatus gameStatus, int cardId, List<Integer> tappingLandIds) {
        castService.cast(gameStatus, cardId, tappingLandIds);
    }

    void resolve(GameStatus gameStatus, String triggeredAction, int cardId) {
        resolveService.resolve(gameStatus, triggeredAction, cardId);
    }

    void declareAttackers(GameStatus gameStatus, List<Integer> cardIds) {
        declareAttackerService.declareAttackers(gameStatus, cardIds);
    }

    void declareBlockers(GameStatus gameStatus, Map<Integer, List<Integer>> targetsIdsForCardIds) {
        declareBlockerService.declareBlockers(gameStatus, targetsIdsForCardIds);
    }
}
