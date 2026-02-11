package application.browser

import com.matag.player.PlayerType
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import java.util.*

class VisibleLibraryHelper(libraryHelper: MatagBrowser?, private val playerType: PlayerType) :
    AbstractCardContainerHelper(libraryHelper) {
    override fun containerElement(): WebElement {
        return matagBrowser.findElement(By.id(playerType.name.lowercase(Locale.getDefault()) + "-library"))
            .findElement(By.className("visible-cards"))
    }
}
