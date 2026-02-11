package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.util.*

class LibraryHelper internal constructor(matagBrowser: MatagBrowser?, private val playerType: PlayerType) :
    AbstractCardContainerHelper(matagBrowser) {
    override fun containerElement(): WebElement? {
        return matagBrowser.findElement(By.id(playerType.name.lowercase(Locale.getDefault()) + "-library"))
    }

    val visibleCardsHelper: VisibleLibraryHelper
        get() = VisibleLibraryHelper(this.matagBrowser, playerType)
}
