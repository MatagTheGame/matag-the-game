package integration

import com.matag.adminentities.PlayerInfo
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstance
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.player.PlayerFactory
import com.matag.game.security.SecurityToken
import com.matag.game.status.GameStatus
import com.matag.game.status.GameStatusFactory
import com.matag.game.turn.phases.main1.Main1Phase
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream

class TestUtils @Autowired constructor(
    private val gameStatusFactory: GameStatusFactory,
    private val playerFactory: PlayerFactory,
    private val cardInstanceFactory: CardInstanceFactory,
    private val cards: Cards
) {
    private var cardInstanceId = 1

    fun testGameStatus(): GameStatus {
        val gameStatus = gameStatusFactory.create("game-id")

        val player1 = PlayerInfo("player-name")
        val player1SecurityToken = SecurityToken("player-session", UUID.randomUUID().toString(), "1")
        gameStatus.player1 = playerFactory.create(player1SecurityToken, player1)
        gameStatus.player1.library.addCards(testLibrary(gameStatus, player1.getPlayerName()))
        gameStatus.player1.drawHand()

        val player2 = PlayerInfo("opponent-name")
        val player2SecurityToken = SecurityToken("opponent-session", UUID.randomUUID().toString(), "1")
        gameStatus.player2 = playerFactory.create(player2SecurityToken, player2)
        gameStatus.player2.library.addCards(testLibrary(gameStatus, player2.getPlayerName()))
        gameStatus.player2.drawHand()

        gameStatus.turn.currentTurnPlayer = "player-name"
        gameStatus.turn.currentPhaseActivePlayer = "player-name"
        gameStatus.turn.currentPhase = Main1Phase.M1

        return gameStatus
    }

    fun createCardInstance(gameStatus: GameStatus, cardName: String, owner: String = "player-name", controller: String = "player-name") =
        cardInstanceFactory.create(gameStatus, cardInstanceId++, cards.get(cardName), owner, controller)

    private fun testLibrary(gameStatus: GameStatus, playerName: String) =
        (1..40).map { cardInstanceFactory.create(gameStatus, cardInstanceId++, cards.get("Plains"), playerName) }
}