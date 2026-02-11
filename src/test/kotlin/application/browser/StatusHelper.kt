package application.browser

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions

class StatusHelper internal constructor(private val matagBrowser: MatagBrowser) {
    fun hasMessage(message: String) {
        matagBrowser.wait(ExpectedConditions.textToBe(By.id("status-message"), message))
    }
}
