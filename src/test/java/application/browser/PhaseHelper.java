package application.browser;

import com.aa.mtg.game.player.PlayerType;
import org.openqa.selenium.By;

import static application.browser.selenium.MtgExpectedCondition.classToContain;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class PhaseHelper {
    private MtgBrowser mtgBrowser;

    PhaseHelper(MtgBrowser mtgBrowser) {
        this.mtgBrowser = mtgBrowser;
    }

    public void is(String phase, PlayerType priority) {
        mtgBrowser.wait(textToBe(By.cssSelector("#turn-phases .active"), phase));
        mtgBrowser.wait(classToContain(By.cssSelector("#turn-phases .active"), getClassNameLinkedToPriority(priority)));
    }

    private String getClassNameLinkedToPriority(PlayerType priority) {
        if (priority == PlayerType.PLAYER) {
            return "active-for-player";
        } else {
            return "active-for-opponent";
        }
    }
}
