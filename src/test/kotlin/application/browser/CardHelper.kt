package application.browser

import com.matag.cards.Card
import com.matag.cards.Card.name
import com.matag.cards.ability.type.AbilityType
import com.matag.player.PlayerType
import lombok.AllArgsConstructor
import org.assertj.core.api.Assertions
import org.openqa.selenium.*
import org.openqa.selenium.support.ui.ExpectedConditions
import java.util.*
import java.util.stream.Collectors
import java.util.stream.Stream

@AllArgsConstructor
class CardHelper {
    private val webElement: WebElement? = null
    private val matagBrowser: MatagBrowser? = null

    fun click() {
        try {
            webElement!!.click()
        } catch (e: ElementClickInterceptedException) {
            matagBrowser!!.getJavascriptExecutor().executeScript("arguments[0].click()", webElement)
        }
    }

    fun tap() {
        playerHasPriority()
        click()
        this.isFrontendTapped
    }

    fun select() {
        playerHasPriority()
        click()
        this.isSelected
    }

    fun target() {
        playerHasPriority()
        click()
        this.isTargeted
    }

    fun declareAsAttacker() {
        declareAsAttackerOrBlocker()
    }

    fun declareAsBlocker() {
        declareAsAttackerOrBlocker()
    }

    val cardId: String?
        get() = webElement!!.getAttribute("id")

    val cardIdNumeric: Int
        get() = this.cardId!!.split("-".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].toInt()

    val isFrontendTapped: Unit
        get() {
            hasClass("frontend-tapped")
        }

    val isNotFrontendTapped: Unit
        get() {
            doesNotHaveClass("frontend-tapped")
        }

    val isTapped: Unit
        get() {
            hasClass("tapped")
            doesNotHaveClass("frontend-tapped")
        }

    val isTappedDoesNotUntapNextTurn: Unit
        get() {
            hasClass("tapped-does-not-untap-next-turn")
        }

    val isNotTapped: Unit
        get() {
            doesNotHaveClass("tapped")
        }

    val isTargeted: Unit
        get() {
            hasClass("targeted")
        }

    val isSelected: Unit
        get() {
            hasClass("selected")
        }

    val isNotSelected: Unit
        get() {
            doesNotHaveClass("selected")
        }

    fun hasSummoningSickness() {
        matagBrowser!!.wait(ExpectedConditions.presenceOfElementLocated(cardCssSelector(".summoning-sickness")))
    }

    fun doesNotHaveSummoningSickness() {
        waitForAbsenceOfElement(cardCssSelector(".summoning-sickness"))
    }

    fun hasFlying() {
        hasClass("flying")
    }

    fun hasDamage(damage: Int) {
        matagBrowser!!.wait(ExpectedConditions.textToBe(cardCssSelector(".damage"), damage.toString()))
    }

    fun doesNotHaveDamage() {
        waitForAbsenceOfElement(cardCssSelector(".damage"))
    }

    fun hasPowerAndToughness(powerAndToughness: String?) {
        matagBrowser!!.wait(
            ExpectedConditions.textToBe(
                cardCssSelector(".power-toughness"),
                powerAndToughness.toString()
            )
        )
    }

    fun parentHasStyle(style: String?) {
        val parent = webElement!!.findElement(By.xpath("./.."))
        Assertions.assertThat(parent.getAttribute("style")).contains(style)
    }

    private fun hasClass(classValue: String) {
        matagBrowser!!.wait(ExpectedConditions.attributeContains(By.id(this.cardId!!), "class", classValue))
    }

    private fun doesNotHaveClass(classValue: String) {
        matagBrowser!!.wait(
            ExpectedConditions.not(
                ExpectedConditions.attributeContains(
                    By.id(this.cardId!!),
                    "class",
                    classValue
                )
            )
        )
    }

    private fun cardCssSelector(cssSelector: String?): By {
        return By.cssSelector("#" + this.cardId + " " + cssSelector)
    }

    private fun waitForAbsenceOfElement(locator: By) {
        try {
            matagBrowser!!.findElement(locator)
            matagBrowser.wait(ExpectedConditions.invisibilityOfElementLocated(locator))
        } catch (e: TimeoutException) {
            println("Element " + locator + " is not present at all. That's okay.")
        } catch (e: NotFoundException) {
            println("Element " + locator + " is not present at all. That's okay.")
        }
    }

    private fun playerHasPriority() {
        PhaseHelper(matagBrowser).isPriority(PlayerType.PLAYER)
    }

    private fun declareAsAttackerOrBlocker() {
        playerHasPriority()
        val cardId = this.cardId
        click()
        matagBrowser!!.wait(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".combat-line #" + cardId)))
    }

    fun hasPlus1Counters(counters: Int) {
        matagBrowser!!.wait(ExpectedConditions.textToBe(cardCssSelector(".plus-1-counter"), counters.toString()))
    }

    fun hasMinus1Counters(counters: Int) {
        matagBrowser!!.wait(ExpectedConditions.textToBe(cardCssSelector(".minus-1-counter"), counters.toString()))
    }

    fun hasKeywordCounters(keywordAbility: AbilityType) {
        matagBrowser!!.wait(
            ExpectedConditions.attributeContains(
                By.cssSelector("#" + this.cardId + " .keyword-counter"),
                "title",
                keywordAbility.name.lowercase(Locale.getDefault())
            )
        )
    }

    companion object {
        fun cardNames(vararg cards: Card?): MutableList<String?> {
            return Stream.of<Card?>(*cards).map<String?>(::com.matag.cards.Card.name).sorted()
                .collect(Collectors.toList())
        }

        fun cardNames(element: WebElement): MutableList<String?> {
            val cardElements: MutableList<WebElement?> = element.findElements(By.className("card"))
            return cardElements.stream()
                .map<String?> { cardElement: WebElement? -> cardElement!!.getAttribute("aria-label") }
                .sorted()
                .collect(Collectors.toList())
        }
    }
}
