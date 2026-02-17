package application.browser

import com.matag.cards.properties.Color
import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PlayerActiveManaHelper internal constructor(private val matagBrowser: MatagBrowser) {
    fun toHaveMana(colors: List<Color>) {
        matagBrowser.wait(ExpectedCondition {
            val imgs = elementContainer().findElements(By.tagName("img"))
            val alts = imgs.map { it.getAttribute("alt")!! }.sorted()
            val expectedColors = colors.map { it.name }.sorted()
            LOGGER.info("alts={}   expectedColors={}", alts, expectedColors)
            expectedColors == alts
        })
    }

    private fun elementContainer(): WebElement =
        matagBrowser.findElement(By.id("player-active-mana"))

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PlayerActiveManaHelper::class.java)
    }
}
