package application.browser

import com.matag.cards.properties.Color
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.function.Function
import java.util.stream.Collectors

class PlayerActiveManaHelper internal constructor(private val matagBrowser: MatagBrowser) {
    fun toHaveMana(colors: MutableList<Color?>) {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            val imgs: MutableList<WebElement?> = this.elementContainer!!.findElements(By.tagName("img"))
            val alts =
                imgs.stream().map<String?>((Function { webElement: WebElement? -> webElement!!.getAttribute("alt") }))
                    .sorted().collect(Collectors.toList())
            val expectedColors =
                colors.stream().map<String?> { obj: Color? -> obj!!.name }.sorted().collect(Collectors.toList())
            LOGGER.info("alts={}   expectedColors={}", alts, expectedColors)
            expectedColors == alts
        })
    }

    private val elementContainer: WebElement?
        get() = matagBrowser.findElement(By.id("player-active-mana"))

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(PlayerActiveManaHelper::class.java)
    }
}
