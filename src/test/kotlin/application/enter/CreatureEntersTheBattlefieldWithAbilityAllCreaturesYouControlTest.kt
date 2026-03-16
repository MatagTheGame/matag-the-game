package application.enter

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Tag("RegressionTests")
@Import(CreatureEntersTheBattlefieldWithAbilityAllCreaturesYouControlTest.InitTestServiceForTest::class)
class CreatureEntersTheBattlefieldWithAbilityAllCreaturesYouControlTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun creatureEntersTheBattlefieldWithAbilityAllCreaturesYouControl() {
        // When Playing Angel of the Dawn
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 3).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 4).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Angel of the Dawn")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)
        val angelOfTheDawnId =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getFirstCard(cards.get("Angel of the Dawn")).getCardIdNumeric()

        // Then the enter the battlefield event gets triggered
        browser.player1().getStackHelper().containsAbility("Player1's Angel of the Dawn (" + angelOfTheDawnId + "): Creatures you control get +1/+1 and vigilance until end of turn.")
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Which increases other creatures
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).hasPowerAndToughness("4/5")

        // And gives them vigilance
        browser.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).declareAsAttacker()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.COMBAT_LINE)
            .getFirstCard(cards.get("Enforcer Griffin")).isNotTapped()
    }

    class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initTestGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Angel of the Dawn"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Enforcer Griffin"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
        }
    }
}
