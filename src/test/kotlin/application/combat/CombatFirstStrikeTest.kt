package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.*
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Tag("RegressionTests")
@Import(CombatFirstStrikeTest.InitTestServiceForTest::class)
class CombatFirstStrikeTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun combatFirstStrike() {
        // Stops to play instants
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(BeginCombatPhase.BC, PlayerType.PLAYER)
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(BeginCombatPhase.BC, PlayerType.OPPONENT)
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )

        // When attacking
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards!!.get("Youthful Knight")).declareAsAttacker()
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.OPPONENT
        )

        // Stop to play instants
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.OPPONENT
        )

        // And blocking
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Coral Merfolk")).declareAsBlocker()
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.PLAYER
        )

        // Then stop to play instants before first strike
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.OPPONENT
        )

        // Then stop to play instants after first strike, before combat damage and at end of combat
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(FirstStrikePhase.FS, PlayerType.PLAYER)
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(FirstStrikePhase.FS, PlayerType.OPPONENT)
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(CombatDamagePhase.CD, PlayerType.PLAYER)
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(
            CombatDamagePhase.CD,
            PlayerType.OPPONENT
        )
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(EndOfCombatPhase.EC, PlayerType.PLAYER)
        browser!!.player1().getActionHelper().clickContinueAndExpectPhase(EndOfCombatPhase.EC, PlayerType.OPPONENT)
        browser!!.player2().getActionHelper().clickContinueAndExpectPhase(Main2Phase.M2, PlayerType.PLAYER)

        // Then only the non first strike creature dies
        browser!!.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Coral Merfolk"))
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Youthful Knight"))
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        public override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Youthful Knight"))
            addCardToCurrentPlayerHand(gameStatus, cards!!.get("Bladebrand"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Swamp"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Coral Merfolk"))
            addCardToNonCurrentPlayerHand(gameStatus, cards!!.get("Bladebrand"))
        }
    }
}
