package application.enter

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import
import java.util.*

@Import(WheneverACreatureEntersTheBattlefieldAbilityTest.InitTestServiceForTest::class)
class WheneverACreatureEntersTheBattlefieldAbilityTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun wheneverACreatureEntersTheBattlefieldAbility() {
        // When Playing Ajani's Welcome
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards!!.get("Plains"), 0).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Ajani's Welcome")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Ajani's Welcome"))

        // And then a creature
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 2).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Daybreak Chaplain")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then a the creature is on the battlefield and event is triggered
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Daybreak Chaplain"))
        val ajanisWelcomeId1 =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getCard(cards.get("Ajani's Welcome"), 0).getCardIdNumeric()
        browser.player1().getStackHelper().containsAbility("Player1's Ajani's Welcome (" + ajanisWelcomeId1 + "): You gain 1 life.")

        // When players continue
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then player 1 gains 1 life
        browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(21)

        // When Playing another Ajani's Welcome
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 3).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Ajani's Welcome")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Ajani's Welcome"))

        // And then another creature
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 4).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 5).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Daybreak Chaplain")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then a both creatures are on the battlefield two events is triggered
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .contains(cards.get("Daybreak Chaplain"), cards.get("Daybreak Chaplain"))
        val ajanisWelcomeId2 =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getCard(cards.get("Ajani's Welcome"), 1).getCardIdNumeric()
        browser.player1().getStackHelper().containsAbilitiesExactly(
            Arrays.asList<String?>(
                "Player1's Ajani's Welcome (" + ajanisWelcomeId1 + "): You gain 1 life.",
                "Player1's Ajani's Welcome (" + ajanisWelcomeId2 + "): You gain 1 life."
            )
        )

        // When players continue
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then player 1 gains 1 life
        browser.player1().getStackHelper().containsAbility("Player1's Ajani's Welcome (" + ajanisWelcomeId1 + "): You gain 1 life.")
        browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(22)

        // When players continue
        browser.player1().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.OPPONENT)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main2Phase.M2, PlayerType.PLAYER)

        // Then player 1 gains 1 life
        browser.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(23)
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            addCardToCurrentPlayerHand(gameStatus, cards.get("Ajani's Welcome"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Ajani's Welcome"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Daybreak Chaplain"))
            addCardToCurrentPlayerHand(gameStatus, cards.get("Daybreak Chaplain"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
        }
    }
}
