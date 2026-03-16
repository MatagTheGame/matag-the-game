package application.counters

import application.AbstractApplicationTest
import application.InitTestService
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.cards.ability.type.AbilityType
import com.matag.game.cardinstance.CardInstanceFactory
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.CombatDamagePhase
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.main1.Main1Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.springframework.context.annotation.Import

@Tag("RegressionTests")
@Import(PutAKeywordCounterTest.InitTestServiceForTest::class)
class PutAKeywordCounterTest(var initService: InitTestService) : AbstractApplicationTest() {

    override fun setupGame() {
        initService = InitTestServiceForTest(cardInstanceFactory, cards)
    }

    @Test
    fun putAKeywordCounterOnCreatureTest() {
        // When cast Blood Curdle
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Plains"), 1).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Swamp"), 0).tap()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.FIRST_LINE)
            .getCard(cards.get("Swamp"), 1).tap()
        browser.player1().getHandHelper(PlayerType.PLAYER).getFirstCard(cards.get("Blood Curdle")).select()
        browser.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Catacomb Crocodile")).click()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Catacomb Crocodile")).target()

        // And opponent accepts
        browser.player2().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser.player2().getActionHelper().clickContinueAndExpectPhase(Main1Phase.M1, PlayerType.PLAYER)

        // Then
        browser.player1().getPhaseHelper().matches(Main1Phase.M1, PlayerType.PLAYER)
        browser.player1().getGraveyardHelper(PlayerType.PLAYER).contains(cards.get("Blood Curdle"))
        browser.player1().getGraveyardHelper(PlayerType.OPPONENT).contains(cards.get("Catacomb Crocodile"))
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Catacomb Crocodile")).hasKeywordCounters(AbilityType.MENACE)

        // When attacking
        browser.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareAttackersPhase.DA,
            PlayerType.PLAYER
        )
        val attackingCreature =
            browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getFirstCard(cards.get("Catacomb Crocodile")).getCardIdNumeric()
        browser.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Catacomb Crocodile")).declareAsAttacker()
        browser.player1().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.OPPONENT
        )

        // When attempting to block
        val blockingCreature =
            browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
                .getFirstCard(cards.get("Catacomb Crocodile")).getCardIdNumeric()
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.SECOND_LINE)
            .getFirstCard(cards.get("Catacomb Crocodile")).declareAsBlocker()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(
            DeclareBlockersPhase.DB,
            PlayerType.OPPONENT
        )

        // Then cannot as the opponent attacker as menace
        browser.player2().getMessageHelper().hasMessage("\"" + blockingCreature + " - Catacomb Crocodile" + "\" cannot block \"" + attackingCreature + " - Catacomb Crocodile\" alone as it has menace.")
        browser.player2().getMessageHelper().close()

        // So need to remove the blocker continue and lose
        browser.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.COMBAT_LINE)
            .getFirstCard(cards.get("Catacomb Crocodile")).click()
        browser.player2().getActionHelper().clickContinueAndExpectPhase(CombatDamagePhase.CD, PlayerType.PLAYER)
        browser.player2().getMessageHelper().hasMessage("Player1 Win! Go back to admin.")
    }

    internal class InitTestServiceForTest(cardInstanceFactory: CardInstanceFactory, cards: Cards) : InitTestService(cardInstanceFactory, cards) {
        override fun initGameStatus(gameStatus: GameStatus) {
            gameStatus.player1?.life = 1
            gameStatus.player2?.life = 1

            addCardToCurrentPlayerHand(gameStatus, cards.get("Blood Curdle"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Plains"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Swamp"))
            addCardToCurrentPlayerBattlefield(gameStatus, cards.get("Catacomb Crocodile"))

            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Catacomb Crocodile"))
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards.get("Catacomb Crocodile"))
        }
    }
}
