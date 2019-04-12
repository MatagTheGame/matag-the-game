package com.aa.mtg.game.turn.action;

import com.aa.mtg.cards.CardInstance;
import com.aa.mtg.cards.Cards;
import com.aa.mtg.game.event.Event;
import com.aa.mtg.game.event.EventSender;
import com.aa.mtg.game.player.Library;
import com.aa.mtg.game.player.Player;
import com.aa.mtg.game.status.GameStatus;
import com.aa.mtg.game.turn.Turn;
import com.aa.mtg.game.turn.phases.Phase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Arrays.asList;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ContinueTurnServiceTest.ContinueTurnServiceTestConfiguration.class)
public class ContinueTurnServiceTest {
    private static final CardInstance A_CARD = new CardInstance(1, Cards.FOREST, "owner");

    @Autowired
    private ContinueTurnService continueTurnService;

    @Autowired
    private EventSender eventSender;

    @Test
    public void testContinueTurnUntapPlayer() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.UT);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(1);
        expectedTurn.setCurrentTurnPlayer("player-name");
        expectedTurn.setCurrentPhase(Phase.UP);
        expectedTurn.setCurrentPhaseActivePlayer("player-name");
        expectedTurn.addCardToCardsPlayedWithinTurn(A_CARD);
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_ACTIVE_PLAYER_BATTLEFIELD", gameStatus.getPlayer1().getBattlefield().getCards())
        );

        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn)
        );
    }

    @Test
    public void testContinueTurnUpkeepPlayer() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.UP);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

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
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.UP);
        turn.setCurrentPhaseActivePlayer("opponent-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

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
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.DR);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

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
    public void testContinueTurnDrawPlayerSecondTurn() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        Turn turn = new Turn();
        turn.setTurnNumber(2);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.DR);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

        // Then
        BDDMockito.verify(eventSender).sendToPlayer(gameStatus.getPlayer1(),
                new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getCurrentPlayer().getHand().getCards()));
        BDDMockito.verify(eventSender).sendToPlayer(gameStatus.getPlayer2(),
                new Event("UPDATE_ACTIVE_PLAYER_HAND", gameStatus.getCurrentPlayer().getHand().maskedHand()));

        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(2);
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
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.M1);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

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
    public void testContinueTurnET() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.ET);
        turn.setCurrentPhaseActivePlayer("player-name");

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(1);
        expectedTurn.setCurrentTurnPlayer("player-name");
        expectedTurn.setCurrentPhase(Phase.ET);
        expectedTurn.setCurrentPhaseActivePlayer("opponent-name");
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn));
    }

    @Test
    public void testContinueTurnETDiscardACard() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        IntStream.rangeClosed(1, 8).forEach(i -> gameStatus.getPlayer1().getHand().addCard(A_CARD));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.ET);
        turn.setCurrentPhaseActivePlayer("player-name");

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(1);
        expectedTurn.setCurrentTurnPlayer("player-name");
        expectedTurn.setCurrentPhase(Phase.ET);
        expectedTurn.setCurrentPhaseActivePlayer("player-name");
        expectedTurn.setTriggeredAction("DISCARD_A_CARD");
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn));
    }

    @Test
    public void testContinueTurnCLPlayer() {
        // Given
        GameStatus gameStatus = new GameStatus("game-id");
        gameStatus.setPlayer1(new Player("player-session", "player-name", library()));
        gameStatus.setPlayer2(new Player("opponent-session", "opponent-name", library()));

        Turn turn = new Turn();
        turn.setTurnNumber(1);
        turn.setCurrentTurnPlayer("player-name");
        turn.setCurrentPhase(Phase.CL);
        turn.setCurrentPhaseActivePlayer("player-name");
        turn.addCardToCardsPlayedWithinTurn(A_CARD);

        ReflectionTestUtils.setField(gameStatus, "turn", turn);

        // When
        continueTurnService.continueTurn(gameStatus);

        // Then
        Turn expectedTurn = new Turn();
        expectedTurn.setTurnNumber(2);
        expectedTurn.setCurrentTurnPlayer("opponent-name");
        expectedTurn.setCurrentPhase(Phase.UT);
        expectedTurn.setCurrentPhaseActivePlayer("opponent-name");
        BDDMockito.verify(eventSender).sendToPlayers(
                asList(gameStatus.getPlayer1(), gameStatus.getPlayer2()),
                new Event("UPDATE_TURN", expectedTurn));
    }

    private Library library() {
        List<CardInstance> libraryCards = IntStream.rangeClosed(1, 60)
                .boxed()
                .map(i -> new CardInstance(i, Cards.PLAINS, "owner"))
                .collect(Collectors.toList());

        return new Library(libraryCards);
    }

    @Configuration
    @ComponentScan(basePackageClasses = {Turn.class, GameStatus.class})
    public static class ContinueTurnServiceTestConfiguration {
        @Bean
        public EventSender eventSender() {
            return Mockito.mock(EventSender.class);
        }
    }
}