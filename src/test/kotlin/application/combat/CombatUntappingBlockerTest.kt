package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.BeginCombatPhase
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Import(CombatUntappingBlockerTest.InitTestServiceForTest::class)
class CombatUntappingBlockerTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun combatUntappingBlocker() {
        // Player1 attacks
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(BeginCombatPhase.BC, PlayerType.OPPONENT)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).click()
        browser.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.OPPONENT
        )

        // Player2 before going to blocking untap its creature
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Forest"), 0).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Forest"), 1).tap()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Forest"), 2).tap()
        browser.player2().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Spidery Grasp")).select()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Ancient Brontodon")).click()
        browser.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.OPPONENT
        )
        browser.player2().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.OPPONENT
        )

        // Now Player2 can block
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Ancient Brontodon")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main2Phase.M2, PlayerType.PLAYER)
        browser.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Headwater Sentries"))
    }

    class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Headwater Sentries"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Forest"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Ancient Brontodon"))
            gameStatus.nonCurrentPlayer.battlefield.search().withName("Ancient Brontodon").cards.get(0)
                .modifiers.isTapped = true
            addCardToNonCurrentPlayerHand(gameStatus, cards.get("Spidery Grasp"))
        }
    }
}
