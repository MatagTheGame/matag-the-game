package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.WebElement

class GraveyardHelper internal constructor(matagBrowser: MatagBrowser?, private val playerType: PlayerType?) :
    AbstractCardContainerHelper(matagBrowser) {
    override fun containerElement(): WebElement? {
        if (playerType == PlayerType.PLAYER) {
            return matagBrowser.findElement(By.id("player-graveyard"))
        } else {
            return matagBrowser.findElement(By.id("opponent-graveyard"))
        }
    }
}
