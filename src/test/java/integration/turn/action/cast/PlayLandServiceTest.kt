package integration.turn.action.cast

import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.turn.action.cast.PlayLandService
import com.matag.game.turn.action.enter.EnterCardIntoBattlefieldService
import integration.TestUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@ContextConfiguration(classes = [CastTestConfiguration::class])
class PlayLandServiceTest {
    @Autowired
    private val playLandService: PlayLandService? = null

    @Autowired
    private val cardInstanceFactory: CardInstanceFactory? = null

    @Autowired
    private val enterCardIntoBattlefieldService: EnterCardIntoBattlefieldService? = null

    @Autowired
    private val testUtils: TestUtils? = null

    @Autowired
    private val cards: Cards? = null

    @Test
    fun playLand() {
        // Given
        val gameStatus = testUtils!!.testGameStatus()
        val card = cardInstanceFactory!!.create(gameStatus, 100, cards!!.get("Swamp"), "player-name")
        gameStatus.getPlayer1().getHand().addCard(card)

        // When
        playLandService!!.playLand(gameStatus, card.getId())

        // Then
        Mockito.verify<EnterCardIntoBattlefieldService?>(enterCardIntoBattlefieldService).enter(gameStatus, card)
    } //  @Test
    //  public void playLandErrorNotALand() {
    //    // Given
    //    var gameStatus = testUtils.testGameStatus();
    //    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Befuddle"), "player-name");
    //    gameStatus.getPlayer1().getHand().addCard(card);
    //
    //    // Expect
    //    thrown.expectMessage("Playing \"100 - Befuddle\" as land.");
    //
    //    // When
    //    playLandService.playLand(gameStatus, card.getId());
    //  }
    //
    //  @Test
    //  public void playLandMultipleLand() {
    //    // Given
    //    var gameStatus = testUtils.testGameStatus();
    //    var card1 = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name");
    //    gameStatus.getPlayer1().getHand().addCard(card1);
    //    var card2 = cardInstanceFactory.create(gameStatus, 101, cards.get("Swamp"), "player-name");
    //    gameStatus.getPlayer1().getHand().addCard(card2);
    //
    //    // When
    //    playLandService.playLand(gameStatus, card1.getId());
    //
    //    // Then
    //    verify(enterCardIntoBattlefieldService).enter(gameStatus, card1);
    //
    //    // Expect
    //    thrown.expectMessage("You already played a land this turn.");
    //
    //    // When
    //    playLandService.playLand(gameStatus, card2.getId());
    //  }
    //
    //  @Test
    //  public void playLandNotInMainPhase() {
    //    // Given
    //    var gameStatus = testUtils.testGameStatus();
    //    gameStatus.getTurn().setCurrentPhase(FS);
    //    var card = cardInstanceFactory.create(gameStatus, 100, cards.get("Swamp"), "player-name");
    //    gameStatus.getPlayer1().getHand().addCard(card);
    //
    //    // Expect
    //    thrown.expectMessage("You can only play lands during main phases.");
    //
    //    // When
    //    playLandService.playLand(gameStatus, card.getId());
    //  }
}