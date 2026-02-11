package application.combat

import application.AbstractApplicationTest
import application.InitTestService
import application.InitTestServiceDecorator
import application.browser.BattlefieldHelper
import com.matag.cards.Cards
import com.matag.game.status.GameStatus
import com.matag.game.turn.phases.combat.DeclareAttackersPhase
import com.matag.game.turn.phases.combat.DeclareBlockersPhase
import com.matag.game.turn.phases.main2.Main2Phase
import com.matag.player.PlayerType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class BasicCombatTest : AbstractApplicationTest() {
    @Autowired
    private val initTestServiceDecorator: InitTestServiceDecorator? = null

    @Autowired
    private val cards: Cards? = null

    public override fun setupGame() {
        initTestServiceDecorator!!.setInitTestService(InitTestServiceForTest())
    }

    @Test
    fun basicCombat() {
        // When continuing
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareAttackersPhase.Companion.DA,
            PlayerType.PLAYER
        )

        // Message and status are about declaring attackers
        browser!!.player1().statusHelper.hasMessage("Choose creatures you want to attack with.")

        // When declare attacker
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards!!.get("Headwater Sentries")).click()

        // Then attacker is moved forward
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).isFrontendTapped

        // When withdraw attacker
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).click()

        // Then attacker is moved backward
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).isNotTapped

        // When declare illegal attacker - already tapped
        val nestRobberId =
            browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
                .getFirstCard(cards.get("Nest Robber")).cardIdNumeric
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Nest Robber")).click()

        // Then a message is displayed
        browser!!.messageHelper.hasMessage("\"" + nestRobberId + " - Nest Robber\" is tapped and cannot attack.")
        browser!!.messageHelper.close()

        // When declare illegal attacker - defender
        val guardiansOfMeletisId =
            browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
                .getFirstCard(cards.get("Guardians of Meletis")).cardIdNumeric
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Guardians of Meletis")).click()

        // Then a message is displayed
        browser!!.messageHelper.hasMessage("\"" + guardiansOfMeletisId + " - Guardians of Meletis\" has defender and cannot attack.")
        browser!!.messageHelper.close()

        // The four attackers are declared as attacker and continue
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).click()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Coral Commando")).click()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Axebane Beast")).click()
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Ancient Brontodon")).click()
        browser!!.player1().actionHelper.clickContinueAndExpectPhase(
            DeclareBlockersPhase.Companion.DB,
            PlayerType.OPPONENT
        )

        // The phase move to Declare blocker
        browser!!.player1().statusHelper.hasMessage("Wait for opponent to perform its action...")
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .toHaveSize(4)
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).isTapped

        // And the opponent sees the same
        browser!!.player2().statusHelper.hasMessage("Choose creatures you want to block with.")
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .toHaveSize(4)
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).isTapped

        // Opponent select a creature to block
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Ancient Brontodon")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Ancient Brontodon")).isSelected

        // Opponent decide to block that creature
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Coral Commando")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .contains(cards.get("Coral Commando"))

        // Opponent decide to withdraw that block
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Coral Commando")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .contains(cards.get("Coral Commando"))

        // Opponent declare blockers
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).parentHasStyle("margin-left: 390px; margin-top: 0px;")

        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Coral Commando")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Coral Commando")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Coral Commando")).parentHasStyle("margin-left: 130px; margin-top: 0px;")

        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Axebane Beast")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Nest Robber")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getFirstCard(cards.get("Nest Robber")).parentHasStyle("margin-left: -130px; margin-top: 0px;")
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).click()
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.COMBAT_LINE)
            .getCard(cards.get("Headwater Sentries"), 1).parentHasStyle("margin-left: -105px; margin-top: 50px;")

        // Continue to M2
        browser!!.player2().actionHelper.clickContinueAndExpectPhase(Main2Phase.Companion.M2, PlayerType.PLAYER)

        // Cards are now
        browser!!.player1().getGraveyardHelper(PlayerType.PLAYER)
            .contains(cards.get("Coral Commando"), cards.get("Axebane Beast"))
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .contains(cards.get("Headwater Sentries"), cards.get("Ancient Brontodon"))
        browser!!.player1().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).hasDamage(2)
        browser!!.player1().getGraveyardHelper(PlayerType.OPPONENT)
            .contains(cards.get("Coral Commando"), cards.get("Nest Robber"))
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .contains(cards.get("Headwater Sentries"), cards.get("Headwater Sentries"))
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Headwater Sentries"), 0).hasDamage(2)
        browser!!.player1().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Headwater Sentries"), 1).hasDamage(2)

        browser!!.player2().getGraveyardHelper(PlayerType.OPPONENT)
            .contains(cards.get("Coral Commando"), cards.get("Axebane Beast"))
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .contains(cards.get("Headwater Sentries"), cards.get("Ancient Brontodon"))
        browser!!.player2().getBattlefieldHelper(PlayerType.OPPONENT, BattlefieldHelper.Companion.SECOND_LINE)
            .getFirstCard(cards.get("Headwater Sentries")).hasDamage(2)
        browser!!.player2().getGraveyardHelper(PlayerType.PLAYER)
            .contains(cards.get("Coral Commando"), cards.get("Nest Robber"))
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .contains(cards.get("Headwater Sentries"), cards.get("Headwater Sentries"))
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Headwater Sentries"), 0).hasDamage(2)
        browser!!.player2().getBattlefieldHelper(PlayerType.PLAYER, BattlefieldHelper.Companion.SECOND_LINE)
            .getCard(cards.get("Headwater Sentries"), 1).hasDamage(2)

        // Life is
        browser!!.player2().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(11)
        browser!!.player2().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(20)

        browser!!.player1().getPlayerInfoHelper(PlayerType.PLAYER).toHaveLife(20)
        browser!!.player1().getPlayerInfoHelper(PlayerType.OPPONENT).toHaveLife(11)
    }

    internal class InitTestServiceForTest : InitTestService() {
        public override fun initGameStatus(gameStatus: GameStatus) {
            // Single block both survive
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Headwater Sentries")) // 2/5
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Headwater Sentries")) // 2/5

            // Single block both die
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Coral Commando")) // 3/2
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Coral Commando")) // 3/2

            // Double block attacker dies one blocker dies
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Axebane Beast")) // 3/4
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Nest Robber")) // 2/1
            addCardToNonCurrentPlayerBattlefield(gameStatus, cards!!.get("Headwater Sentries")) // 2/5

            // Non blocked damage to player
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Ancient Brontodon")) // 9/9

            // Cannot attack as tapped
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Nest Robber"))
            gameStatus.currentPlayer.battlefield.cards.get(4).modifiers.setTapped(true)

            // Cannot attack as defender
            addCardToCurrentPlayerBattlefield(gameStatus, cards!!.get("Guardians of Meletis"))
            gameStatus.currentPlayer.battlefield.cards.get(4).modifiers.setTapped(true)
        }
    }
}
