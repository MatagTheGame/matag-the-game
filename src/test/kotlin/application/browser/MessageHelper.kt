package application.browser

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.ExpectedConditions

class MessageHelper internal constructor(private val matagBrowser: MatagBrowser) {
    fun hasMessage(message: String) {
        matagBrowser.wait(ExpectedConditions.textToBe(By.id("message-text"), message))
    }

    fun hasNoMessage() {
        matagBrowser.wait(ExpectedConditions.invisibilityOfElementLocated(By.id("message")))
    }

    fun close() {
        matagBrowser.findElement(By.id("popup-close")).click()
    }
}
