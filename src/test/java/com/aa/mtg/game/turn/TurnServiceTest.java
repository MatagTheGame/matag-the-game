package com.aa.mtg.game.turn;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.event.Event;
import com.aa.mtg.event.EventSender;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.phases.Phase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import static java.util.Arrays.asList;

@RunWith(MockitoJUnitRunner.class)
public class TurnServiceTest {

    @Mock
    private EventSender eventSender;

    @Mock
    private Library library;

    @InjectMocks
    private TurnService turnService;

    private final CardInstance A_CARD = new CardInstance(1, Cards.FOREST);

    @Test
    public void testContinueTurnUpkeepPlayer() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.UP);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        turnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(1);
        expectedTurn.setCurrentTurnPlayer("player-name");
        expectedTurn.setCurrentPhase(Phase.UP);
        expectedTurn.setCurrentPhaseActivePlayer("opponent-name");
        expectedTurn.addCardToCardsPlayedWithinTurn(A_CARD);
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn)
        );
    }

    @Test
    public void testContinueTurnUpkeepOpponent() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.UP);
        turn.setCurrentPhaseActivePlayer("opponent-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        turnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(1);
        expectedTurn.setCurrentTurnPlayer("player-name");
        expectedTurn.setCurrentPhase(Phase.DR);
        expectedTurn.setCurrentPhaseActivePlayer("player-name");
        expectedTurn.addCardToCardsPlayedWithinTurn(A_CARD);
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn)
        );
    }

    @Test
    public void testContinueTurnDrawPlayer() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.DR);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        turnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(1);
        expectedTurn.setCurrentTurnPlayer("player-name");
        expectedTurn.setCurrentPhase(Phase.M1);
        expectedTurn.setCurrentPhaseActivePlayer("player-name");
        expectedTurn.addCardToCardsPlayedWithinTurn(A_CARD);
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn));
    }

    @Test
    public void testContinueTurnM1Player() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.M1);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        turnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(1);
        expectedTurn.setCurrentTurnPlayer("player-name");
        expectedTurn.setCurrentPhase(Phase.M1);
        expectedTurn.setCurrentPhaseActivePlayer("opponent-name");
        expectedTurn.addCardToCardsPlayedWithinTurn(A_CARD);
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn));
    }

    @Test
    public void testContinueTurnCLPlayer() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.CL);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        turnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(2);
        expectedTurn.setCurrentTurnPlayer("opponent-name");
        expectedTurn.setCurrentPhase(Phase.UP);
        expectedTurn.setCurrentPhaseActivePlayer("opponent-name");
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn));
    }
}