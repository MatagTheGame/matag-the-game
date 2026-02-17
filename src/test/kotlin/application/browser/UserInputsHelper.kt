package application.browser

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.ui.ExpectedCondition
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class UserInputsHelper internal constructor(private val matagBrowser: MatagBrowser) {
    fun toHaveAbilities(expectedAbilities: List<String>) {
        matagBrowser.wait(ExpectedCondition {
            val abilities = userInputs().map { it.getAttribute("title") }
            LOGGER.info("abilities={}   expectedAbilities={}", abilities, expectedAbilities)
            expectedAbilities == abilities
        })
    }

    fun choose(index: Int) {
        userInputs()[index].click()
    }

    private fun userInputs(): List<WebElement> {
        return elementContainer().findElements(By.tagName("li"))
    }

    private fun elementContainer(): WebElement =
        matagBrowser.findElement(By.id("user-inputs"))

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(UserInputsHelper::class.java)
    }
}
