package ui_testing_playground;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class ProgressBarTests extends UiTestingPlaygroundParent {
    @BeforeClass
    public void navigateToSampleApp() {
        page.querySelector("a:has-text('Progress Bar')").click();
    }

    @Test
    public void progressBarTest() {
        page.querySelector("button#startButton").click();

        // using the following line to detect the 75 % value shows to be unstable
        // when the progress bar sometimes loads faster it does not detect the value
        // page.waitForSelector("div.progress-bar[aria-valuenow='75']");

        int progressBarValue = 0;
        while(progressBarValue < 75) {
            String progressBarValueString = page.querySelector("div.progress-bar").innerText();
            progressBarValue = Integer.parseInt(progressBarValueString.replace("%", ""));
        }

        page.querySelector("button#stopButton").click();
    }
}
