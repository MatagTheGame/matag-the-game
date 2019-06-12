package application.browser.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

import java.util.Arrays;

public class MtgExpectedCondition {
    public static ExpectedCondition<Boolean> classToContain(final By locator, final String value) {
        return new ExpectedCondition<Boolean>() {
            private String currentValue = null;

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    currentValue = driver.findElement(locator).getAttribute("class");
                    return Arrays.asList(currentValue.split(" ")).contains(value);
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("element found by %s to have attribute[class] containing \"%s\". attribute[class]=\"%s\"",
                        locator, value, currentValue);
            }
        };
    }
}
