import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.ElementHandle;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.PlaywrightException;
import org.json.simple.JSONArray;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertTrue;

public class PlayerNationalityTest extends VerifyJSONDataParent {
    @Test
    public void verifyPlayerNationalityTest() {
        String url = getURLFromOldestTeam(teamsOnlyJSON);

        try {
            Playwright playwright = Playwright.create();
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            Page page = browser.newPage();
            page.navigate(url);

            page.querySelector("button:has-text('Équipe')").click();
            page.querySelector("a:has-text('Formation')").click();

            page.waitForSelector("table");
            List<ElementHandle> nationalities = page.querySelectorAll("table.rt-table tr.rt-tr td.rt-td:last-child div span:last-child");

            int countOfCanadianPlayers = 0;
            int countOfUSAPlayers = 0;
            for (ElementHandle nationality: nationalities) {
                String nationalityString = nationality.innerText();
                if (nationalityString.equals("CAN")) {
                    countOfCanadianPlayers++;
                } else if (nationalityString.equals("USA")) {
                    countOfUSAPlayers++;
                }
            }

            assertTrue(countOfCanadianPlayers > countOfUSAPlayers, "There are not more Canadian players than players from USA.");

            browser.close();
            playwright.close();
        } catch (PlaywrightException e) {
            e.printStackTrace();
        }
    }

    private static String getURLFromOldestTeam(JSONArray teamsOnlyJSON) {
        String oldestTeamFromRequest = findOldestTeam(teamsOnlyJSON);
        return getURLFromTeamName(teamsOnlyJSON, oldestTeamFromRequest);
    }

    private static String getURLFromTeamName(JSONArray teamsOnlyJSON, String targetTeamName) {
        for (int i = 0; i < teamsOnlyJSON.size(); i++) {
            String teamName = getTeamName(teamsOnlyJSON, i);
            if (teamName.equals(targetTeamName)) {
                return getURLFromIndex(teamsOnlyJSON, i);
            }
        }
        return null;
    }

    private static String getURLFromIndex(JSONArray teamsOnlyJSON, int indexOfTeam) {
        return (String) getTeamAttribute(teamsOnlyJSON, indexOfTeam, 4);
    }
}
