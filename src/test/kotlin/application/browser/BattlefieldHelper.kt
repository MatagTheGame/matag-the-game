package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

class BattlefieldHelper internal constructor(
    matagBrowser: MatagBrowser?,
    private val playerType: PlayerType?,
    private val lineType: String
) : AbstractCardContainerHelper(matagBrowser) {
    override fun containerElement(): WebElement {
        return playerTypeContainer()!!.findElement(By.className(lineType))
    }

    private fun playerTypeContainer(): WebElement? {
        if (playerType == PlayerType.PLAYER) {
            return matagBrowser.findElement(By.id("player-battlefield"))
        } else {
            return matagBrowser.findElement(By.id("opponent-battlefield"))
        }
    }

    companion object {
        const val FIRST_LINE: String = "first-line"
        const val SECOND_LINE: String = "second-line"
        const val COMBAT_LINE: String = "combat-line"
    }
}
