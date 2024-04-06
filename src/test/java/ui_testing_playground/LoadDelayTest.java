package ui_testing_playground;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.LoadState;
import org.testng.annotations.Test;

public class LoadDelayTest extends UiTestingPlaygroundParent {
    @Test
    public void verifyLoadDelayTest() {
        page.querySelector("a:has-text('Load Delay')").click();
        page.waitForLoadState(LoadState.LOAD, new Page.WaitForLoadStateOptions().setTimeout(10000));
        page.waitForSelector("button.btn-primary");
    }
}
