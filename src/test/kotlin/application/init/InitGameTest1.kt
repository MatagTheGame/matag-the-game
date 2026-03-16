package application.init

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Import(InitGameTest.InitTestServiceForTest::class)
class InitGameTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
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
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .containsExactly(cards.get("Plains"), cards.get("Plains"))
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .containsExactly(cards.get("Grazing Whiptail"))
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Grazing Whiptail")).hasPowerAndToughness("3/4")
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.FIRST_LINE)
            .containsExactly(cards.get("Plains"), cards.get("Plains"))
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .containsExactly(cards.get("Grazing Whiptail"))
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
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
        browser!!.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser!!.player1().getStatusHelper().hasMessage("Play any spell or ability or continue (SPACE).")
        browser!!.player1().getActionHelper().canContinue()
        browser!!.player2().getPhaseHelper().matches(Main1Phase.M1, PlayerType.OPPONENT)
        browser!!.player2().getStatusHelper().hasMessage("Wait for opponent to perform its action...")
        browser!!.player2().getActionHelper().cannotContinue()
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
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
