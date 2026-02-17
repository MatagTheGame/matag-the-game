package application.browser

import com.matag.cards.Card
import com.matag.cards.ability.type.AbilityType
import com.matag.player.PlayerType
import org.assertj.core.api.Assertions
import org.openqa.selenium.*
import org.openqa.selenium.support.ui.ExpectedConditions
import java.util.*

class CardHelper(
    private val webElement: WebElement,
    private val matagBrowser: MatagBrowser
) {
    fun click() {
        try {
            webElement.click()
        } catch (_: ElementClickInterceptedException) {
            matagBrowser.getJavascriptExecutor().executeScript("arguments[0].click()", webElement)
        }
    }

    fun tap() {
        playerHasPriority()
        click()
        isFrontendTapped()
    }

    fun select() {
        playerHasPriority()
        click()
        isSelected()
    }

    fun target() {
        playerHasPriority()
        click()
        isTargeted()
    }

    fun declareAsAttacker() {
        declareAsAttackerOrBlocker()
    }

    fun declareAsBlocker() {
        declareAsAttackerOrBlocker()
    }

    fun getCardId(): String =
        webElement.getAttribute("id")!!

    fun getCardIdNumeric(): Int =
        getCardId().split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()

    fun isFrontendTapped() =
        hasClass("frontend-tapped")

    fun isNotFrontendTapped() =
        doesNotHaveClass("frontend-tapped")

    fun isTapped() =
         hasClass("tapped") && doesNotHaveClass("frontend-tapped")

    fun isTappedDoesNotUntapNextTurn() =
        hasClass("tapped-does-not-untap-next-turn")

    fun isNotTapped() =
        doesNotHaveClass("tapped")

    fun isTargeted() =
        hasClass("targeted")

    fun isSelected() =
        hasClass("selected")

    fun isNotSelected() =
        doesNotHaveClass("selected")

    fun hasSummoningSickness() =
        matagBrowser.wait(ExpectedConditions.presenceOfElementLocated(cardCssSelector(".summoning-sickness")))

    fun doesNotHaveSummoningSickness() =
        waitForAbsenceOfElement(cardCssSelector(".summoning-sickness"))

    fun hasFlying() =
        hasClass("flying")

    fun hasDamage(damage: Int) =
        matagBrowser.wait(ExpectedConditions.textToBe(cardCssSelector(".damage"), damage.toString()))

    fun doesNotHaveDamage() =
        waitForAbsenceOfElement(cardCssSelector(".damage"))

    fun hasPowerAndToughness(powerAndToughness: String) =
        matagBrowser.wait(
            ExpectedConditions.textToBe(
                cardCssSelector(".power-toughness"),
                powerAndToughness
            )
        )

    fun parentHasStyle(style: String?) {
        val parent = webElement.findElement(By.xpath("./.."))
        Assertions.assertThat(parent.getAttribute("style")).contains(style)
    }

    private fun hasClass(classValue: String) =
        matagBrowser.wait(ExpectedConditions.attributeContains(By.id(getCardId()), "class", classValue)).let { true }

    private fun doesNotHaveClass(classValue: String) =
        matagBrowser.wait(ExpectedConditions.not(ExpectedConditions.attributeContains(By.id(getCardId()), "class", classValue))).let { true }

    private fun cardCssSelector(cssSelector: String?): By =
        By.cssSelector("#" + getCardId() + " " + cssSelector)

    private fun waitForAbsenceOfElement(locator: By) {
        try {
            matagBrowser.findElement(locator)
            matagBrowser.wait(ExpectedConditions.invisibilityOfElementLocated(locator))
        } catch (_: TimeoutException) {
            println("Element $locator is not present at all. That's okay.")
        } catch (_: NotFoundException) {
            println("Element $locator is not present at all. That's okay.")
        }
    }

    private fun playerHasPriority() =
        PhaseHelper(matagBrowser).isPriority(PlayerType.PLAYER)

    private fun declareAsAttackerOrBlocker() {
        playerHasPriority()
        val cardId = getCardId()
        click()
        matagBrowser.wait(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".combat-line #$cardId")))
    }

    fun hasPlus1Counters(counters: Int) =
        matagBrowser.wait(ExpectedConditions.textToBe(cardCssSelector(".plus-1-counter"), counters.toString()))

    fun hasMinus1Counters(counters: Int) =
        matagBrowser.wait(ExpectedConditions.textToBe(cardCssSelector(".minus-1-counter"), counters.toString()))

    fun hasKeywordCounters(keywordAbility: AbilityType) =
        matagBrowser.wait(
            ExpectedConditions.attributeContains(
                By.cssSelector("#" + getCardId() + " .keyword-counter"),
                "title",
                keywordAbility.name.lowercase(Locale.getDefault())
            )
        )

    companion object {
        fun cardNames(cards: List<Card>): List<String> =
            cards.map { it.name }.sorted()

        fun cardNames(element: WebElement): List<String> {
            val cardElements = element.findElements(By.className("card"))
            return cardElements
                .map { it.getAttribute("aria-label")!! }
                .sorted()
        }
    }
}
