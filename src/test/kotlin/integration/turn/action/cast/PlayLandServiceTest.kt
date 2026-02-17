package integration.turn.action.cast

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.cast.PlayLandService
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService
import integration.TestUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CastTestConfiguration::class])
class PlayLandServiceTest(
    @param:Autowired private val playLandService: PlayLandService,
    @param:Autowired private val cardInstanceFactory: CardInstanceFactory,
    @param:Autowired private val enterCardIntoBattlefieldService: EnterCardIntoBattlefieldService,
    @param:Autowired private val testUtils: TestUtils,
    @param:Autowired private val cards: Cards
) {

    @Test
    fun playLand() {
        // Given
        val gameStatus = testUtils.testGameStatus()
        val card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name")
        gameStatus.player1!!.hand.addCard(card)

        // When
        playLandService.playLand(gameStatus, card.id)

        // Then
        verify(enterCardIntoBattlefieldService).enter(gameStatus, card)
    }

    //  @Test
    //  public void playLandErrorNotALand() {
    //    // Given
    //    var gameStatus = testUtils.testGameStatus();
    //    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Befuddle"), "player-name");
    //    gameStatus.player1.hand.addCard(card);
    //
    //    // Expect
    //    thrown.expectMessage("Playing \"100 - Befuddle\" as land.");
    //
    //    // When
    //    playLandService.playLand(gameStatus, card.id);
    //  }
    //
    //  @Test
    //  public void playLandMultipleLand() {
    //    // Given
    //    var gameStatus = testUtils.testGameStatus();
    //    var card1 = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name");
    //    gameStatus.player1.hand.addCard(card1);
    //    var card2 = cardInstanceFactory.create(gameStatus, 101, cards.get("Swamp"), "player-name");
    //    gameStatus.player1.hand.addCard(card2);
    //
    //    // When
    //    playLandService.playLand(gameStatus, card1.id);
    //
    //    // Then
    //    verify(enterCardIntoBattlefieldService).enter(gameStatus, card1);
    //
    //    // Expect
    //    thrown.expectMessage("You already played a land this turn.");
    //
    //    // When
    //    playLandService.playLand(gameStatus, card2.id);
    //  }
    //
    //  @Test
    //  public void playLandNotInMainPhase() {
    //    // Given
    //    var gameStatus = testUtils.testGameStatus();
    //    gameStatus.turn.setCurrentPhase(FS);
    //    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name");
    //    gameStatus.player1.hand.addCard(card);
    //
    //    // Expect
    //    thrown.expectMessage("You can only play lands during main phases.");
    //
    //    // When
    //    playLandService.playLand(gameStatus, card.id);
    //  }
}