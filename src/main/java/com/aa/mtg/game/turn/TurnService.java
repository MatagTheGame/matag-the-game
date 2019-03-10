package com.aa.mtg.game.turn;

import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.action.CastService;
import com.aa.mtg.game.turn.action.ContinueTurnService;
import com.aa.mtg.game.turn.action.PlayLandService;
import com.aa.mtg.game.turn.action.ResolveService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TurnService {

    private final ContinueTurnService continueTurnService;
    private final PlayLandService playLandService;
    private final CastService castService;
    private final ResolveService resolveService;

    public TurnService(ContinueTurnService continueTurnService, PlayLandService playLandService, CastService castService, ResolveService resolveService) {
        this.continueTurnService = continueTurnService;
        this.playLandService = playLandService;
        this.castService = castService;
        this.resolveService = resolveService;
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
}
