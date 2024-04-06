package ui_testing_playground;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class SampleAppTests extends UiTestingPlaygroundParent {
    @BeforeClass
    public void navigateToSampleApp() {
        page.querySelector("a:has-text('Sample App')").click();
    }

    @Test
    public void verifySuccessfulLoginAndLogoutTest() {
        String username = "zcervinka";
        String password = "pwd";
        loginWithCredentials(username, password);

        String loginStatus = page.querySelector("label#loginstatus").innerText();
        assertTrue(loginStatus.equals("Welcome, " + username + "!"), "User should be logged in but is not.");

        page.querySelector("button#login").click();
        loginStatus = page.querySelector("label#loginstatus").innerText();
        assertTrue(loginStatus.equals("User logged out."), "User should be logged out but is not.");
    }

    @Test
    public void verifyEmptyUserNameTest() {
        String username = null;
        String password = "pwd";
        loginWithCredentials(username, password);

        String loginStatus = page.querySelector("label#loginstatus").innerText();
        assertTrue(loginStatus.equals("Invalid username/password"), "User should be logged in but is not.");
    }

    @Test
    public void verifyEmptyPasswordTest() {
        String username = "zcervinka";
        String password = null;
        loginWithCredentials(username, password);

        String loginStatus = page.querySelector("label#loginstatus").innerText();
        assertTrue(loginStatus.equals("Invalid username/password"), "User should be logged in but is not.");
    }

    @Test
    public void verifyEmptyBothCredentialsTest() {
        String username = null;
        String password = null;
        loginWithCredentials(username, password);

        String loginStatus = page.querySelector("label#loginstatus").innerText();
        assertTrue(loginStatus.equals("Invalid username/password"), "User should be logged in but is not.");
    }

    private static void loginWithCredentials(String username, String password) {
        if (username != null) {
            page.waitForSelector("input[name='UserName']");
            page.querySelector("input[name='UserName']").click();
            page.querySelector("input[name='UserName']").fill(username);
        }

        if (password != null) {
            page.waitForSelector("input[name='Password']");
            page.querySelector("input[name='Password']").click();
            page.querySelector("input[name='Password']").fill(password);
        }

        page.querySelector("button#login").click();
    }
}
