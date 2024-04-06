package ui_testing_playground;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class UiTestingPlaygroundParent {
    protected static String URL = "http://uitestingplayground.com/";
    protected static Playwright playwright;
    protected static Browser browser;
    protected static Page page;

    @BeforeClass
    public void openUrl() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate(URL);
    }

    @AfterClass
    public void closeBrowser() {
        browser.close();
        playwright.close();
    }
}
