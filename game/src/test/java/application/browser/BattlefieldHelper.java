package application.browser;

import com.aa.mtg.player.PlayerType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class BattlefieldHelper extends AbstractCardContainerHelper {
    public static final String FIRST_LINE = "first-line";
    public static final String SECOND_LINE = "second-line";
    public static final String COMBAT_LINE = "combat-line";

    private final PlayerType playerType;
    private final String lineType;

    BattlefieldHelper(MtgBrowser mtgBrowser, PlayerType playerType, String lineType) {
        super(mtgBrowser);
        this.playerType = playerType;
        this.lineType = lineType;
    }

    @Override
    protected WebElement containerElement() {
        return playerTypeContainer().findElement(By.className(lineType));
    }

    private WebElement playerTypeContainer() {
        if (playerType == PlayerType.PLAYER) {
            return mtgBrowser.findElement(By.id("player-battlefield"));
        } else {
            return mtgBrowser.findElement(By.id("opponent-battlefield"));
        }
    }
}
