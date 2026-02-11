package application.browser

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

class UserInputsHelper internal constructor(private val matagBrowser: MatagBrowser) {
    fun toHaveAbilities(expectedAbilities: MutableList<String?>) {
        matagBrowser.wait(ExpectedCondition { driver: WebDriver? ->
            val abilities =
                userInputs().stream().map<String?> { ability: WebElement? -> ability!!.getAttribute("title") }
                    .collect(Collectors.toList())
            LOGGER.info("abilities={}   expectedAbilities={}", abilities, expectedAbilities)
            expectedAbilities == abilities
        })
    }

    fun choose(index: Int) {
        userInputs().get(index)!!.click()
    }

    private fun userInputs(): MutableList<WebElement?> {
        return this.elementContainer!!.findElements(By.tagName("li"))
    }

    private val elementContainer: WebElement?
        get() = matagBrowser.findElement(By.id("user-inputs"))

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(UserInputsHelper::class.java)
    }
}
