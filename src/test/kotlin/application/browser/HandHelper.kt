package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

class HandHelper internal constructor(matagBrowser: MatagBrowser, private val playerType: PlayerType) :
    AbstractCardContainerHelper(matagBrowser) {
    override fun containerElement(): WebElement {
        return if (playerType == PlayerType.PLAYER) {
            matagBrowser.findElement(By.id("player-hand"))
        } else {
            matagBrowser.findElement(By.id("opponent-hand"))
        }
    }
}
