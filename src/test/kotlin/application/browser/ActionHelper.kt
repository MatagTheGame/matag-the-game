package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions

class ActionHelper internal constructor(private val matagBrowser: MatagBrowser) {
    fun canContinue() {
        matagBrowser.wait(ExpectedConditions.elementToBeClickable(this.continueSelector))
    }

    fun cannotContinue() {
        matagBrowser.wait(ExpectedConditions.not(ExpectedConditions.elementToBeClickable(this.continueSelector)))
    }

    fun clickContinue() {
        matagBrowser.getPhaseHelper().isPriority(PlayerType.PLAYER)
        canContinue()
        matagBrowser.findElement(this.continueSelector).click()
    }

    fun clickContinueAndExpectPhase(expectStatus: String?, player: PlayerType?) {
        clickContinue()
        matagBrowser.player1().getPhaseHelper().`is`(expectStatus, player)
        matagBrowser.player2().getPhaseHelper().`is`(expectStatus, other(player))
    }

    private val continueSelector: By
        get() = By.id("continue-button")

    private fun other(player: PlayerType?): PlayerType {
        if (PlayerType.PLAYER == player) {
            return PlayerType.OPPONENT
        } else {
            return PlayerType.PLAYER
        }
    }
}
