package application.init

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class InitGameTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun display() {
        // Hands are
        browser!!.player1().getHandHelper(PlayerType.PLAYER)
            .containsExactly(cards!!.get("Island"), cards.get("Legion's Judgment"))
        browser!!.player1().getHandHelper(PlayerType.OPPONENT).containsExactly("card", "card")
        browser!!.player2().getHandHelper(PlayerType.PLAYER)
            .containsExactly(cards.get("Forest"), cards.get("Charging Monstrosaur"))
        browser!!.player2().getHandHelper(PlayerType.OPPONENT).containsExactly("card", "card")

        // Battlefields are
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.FIRST_LINE)
            .containsExactly(cards.get("Plains"), cards.get("Plains"))
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .containsExactly(cards.get("Grazing Whiptail"))
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("3/4")
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.FIRST_LINE)
            .containsExactly(cards.get("Plains"), cards.get("Plains"))
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .containsExactly(cards.get("Grazing Whiptail"))
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("3/4")

        // Graveyards are
        browser!!.player1().getGraveyardHelper(PlayerType.PLAYER).containsExactly(cards.get("Plains"))
        browser!!.player1().getGraveyardHelper(PlayerType.OPPONENT).containsExactly(cards.get("Mountain"))
        browser!!.player2().getGraveyardHelper(PlayerType.PLAYER).containsExactly(cards.get("Mountain"))
        browser!!.player2().getGraveyardHelper(PlayerType.OPPONENT).containsExactly(cards.get("Plains"))

        // PlayerInfos are
        browser!!.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveName("Player1")
        browser!!.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(20)
        browser!!.player1().getPlayerInfoHelper(PlayerType.PLAYER).toBeActive()
        browser!!.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveName("Player2")
        browser!!.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(20)
        browser!!.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toBeInactive()
        browser!!.player2().getPlayerInfoHelper(PlayerType.PLAYER).toHaveName("Player2")
        browser!!.player2().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(20)
        browser!!.player2().getPlayerInfoHelper(PlayerType.PLAYER).toBeInactive()
        browser!!.player2().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveName("Player1")
        browser!!.player2().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(20)
        browser!!.player2().getPlayerInfoHelper(PlayerType.OPPONENT).toBeActive()

        // Phase and statuses are
        browser!!.player1().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.PLAYER)
        browser!!.player1().statusHelper.hasMessage("Play any spell or ability or continue (SPACE).")
        browser!!.player1().actionHelper.canContinue()
        browser!!.player2().phaseHelper.`is`(Main1Phase.Companion.M1, PlayerType.OPPONENT)
        browser!!.player2().statusHelper.hasMessage("Wait for opponent to perform its action...")
        browser!!.player2().actionHelper.cannotContinue()
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            // Current Player
            addCardToCurrentPlayerLibrary(gameStatus, cards!!.get("Plains"))
            addCardToCurrentPlayerLibrary(gameStatus, cards!!.get("Island"))

            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Huatli's Snubhorn"))

            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Island"))
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Legion's Judgment"))

            addCardToCurrentPlayerGraveyard(gameStatus, cards!!.get("Plains"))

            // Non Current Player
            addCardToNonCurrentPlayerLibrary(gameStatus, cards!!.get("Mountain"))
            addCardToNonCurrentPlayerLibrary(gameStatus, cards!!.get("Forest"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Mountain"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Grazing Whiptail"))

            addCardToNonCurrentPlayerHand(gameStatus, cards!!.get("Forest"))
            addCardToNonCurrentPlayerHand(gameStatus, cards!!.get("Charging Monstrosaur"))

            addCardToNonCurrentPlayerGraveyard(gameStatus, cards!!.get("Mountain"))
        }
    }
}
