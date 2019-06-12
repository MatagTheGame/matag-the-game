package application.browser;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class StackHelper extends AbstractCardContainerHelper {
    StackHelper(MtgBrowser mtgBrowser) {
        super(mtgBrowser);
    }

    @Override
    protected WebElement containerElement() {
        return mtgBrowser.findElement(By.id("stack"));
    }
}
